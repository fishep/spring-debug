// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.fishep.flink.cdc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fishep.flink.cdc.converter.DateToStringConverter;
import com.fishep.flink.cdc.dbsync.JdbcUtil;
import org.apache.doris.flink.cfg.DorisExecutionOptions;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.cfg.DorisReadOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.doris.flink.sink.writer.serializer.JsonDebeziumSchemaSerializer;
import org.apache.doris.flink.sink.writer.serializer.SimpleStringSerializer;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cdc.connectors.mysql.source.MySqlSource;
import org.apache.flink.cdc.connectors.mysql.table.StartupOptions;
import org.apache.flink.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/***
 *
 * Synchronize the full database through flink cdc
 *
 */
public class DatabaseFullSync {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseFullSync.class);
    private static String MYSQL_HOST = "mysql.dev";
    private static String MYSQL_USER = "demo";
    private static String MYSQL_PASSWD = "demo";
    private static int MYSQL_PORT = 3306;

    private static String FENODES = "doris.dev:8030";
    private static String BENODES = "doris.dev:8040";
    private static String DORIS_USER = "root";
    private static String DORIS_PASSWD = "";

    private static String SYNC_DB = "demo";
    private static String SYNC_TBLS = "demo.*";
    private static String TARGET_DORIS_DB = "demo";

    public static void main(String[] args) throws Exception {
        Map<String, Object> customConverterConfigs = new HashMap<>();
        customConverterConfigs.put(JsonConverterConfig.DECIMAL_FORMAT_CONFIG, "numeric");

        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
            .hostname(MYSQL_HOST)
            .port(MYSQL_PORT)
            .databaseList(SYNC_DB) // set captured database
            .tableList(SYNC_TBLS) // set captured table
            .username(MYSQL_USER)
            .password(MYSQL_PASSWD)
            .serverTimeZone("UTC")
//            .startupOptions(StartupOptions.latest())
            .startupOptions(StartupOptions.initial())
            .debeziumProperties(DateToStringConverter.DEFAULT_PROPS)
            .deserializer(new JsonDebeziumDeserializationSchema(false, customConverterConfigs))
            .includeSchemaChanges(true)
            .scanNewlyAddedTableEnabled(true)
            .build();

        Configuration config = new Configuration();
        config.set(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.setParallelism(1);
        // enable checkpoint
        env.enableCheckpointing(10000);

        DataStreamSource<String> cdcSource = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL CDC Source");

        cdcSource.print("binlog info: ");

        //get table list
        List<String> tableList = getTableList();
        LOG.info("sync table list:{}", tableList);
        for (String tbl : tableList) {
            SingleOutputStreamOperator<String> filterStream = filterTableData(cdcSource, tbl);
            SingleOutputStreamOperator<String> dataStream = addCustomColumn(filterStream);
            DorisSink<String> dorisSink = buildSchemaDorisSink(tbl);
            dataStream.sinkTo(dorisSink).name("sink " + tbl);
        }

        SingleOutputStreamOperator<String> filterCrud = filterCrud(cdcSource);
        SingleOutputStreamOperator<String> changeLog = mapToTableRowChangeLog(filterCrud);
        DorisSink<String> dorisSink = buildSimpleDorisSink("table_row_change_log");
        changeLog.sinkTo(dorisSink).name("sink table_row_change_log");

        env.execute("Full Database Sync ");
    }

    /**
     * Divide according to tablename
     */
    private static SingleOutputStreamOperator<String> filterTableData(SingleOutputStreamOperator<String> stream, String table) {
        return stream.filter((FilterFunction<String>) row -> {
            try {
                JSONObject rowJson = JSON.parseObject(row);
                JSONObject source = rowJson.getJSONObject("source");
                String tbl = source.getString("table");
                return table.equals(tbl);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        });
    }

    private static SingleOutputStreamOperator<String> addCustomColumn(SingleOutputStreamOperator<String> stream) {
        return stream.map((MapFunction<String, String>) row -> {
            JSONObject rowJson = JSON.parseObject(row);
            String op = rowJson.getString("op");
            JSONObject source = rowJson.getJSONObject("source");
            Long timestamp = source.getLong("ts_ms");
            String lastUpdateAt = timestampToString(timestamp);

            /** 同步数据表的结构
             *  新增字段
             *  删除字段
             */
            if (op == null) {
                JSONObject historyRecord = rowJson.getJSONObject("historyRecord");
                if (historyRecord != null) {
                    JSONArray tableChanges = historyRecord.getJSONArray("tableChanges");
                    for (int i = 0; i < tableChanges.size(); i++) {
                        JSONObject tableChange = tableChanges.getJSONObject(i);
                        JSONObject table = tableChange.getJSONObject("table");
                        JSONArray columns = table.getJSONArray("columns");

                        JSONObject lastUpdateAtColumn = new JSONObject(true);
                        lastUpdateAtColumn.put("name", "last_update_at");
                        lastUpdateAtColumn.put("jdbcType", 93);
                        lastUpdateAtColumn.put("typeName", "DATETIME");
                        lastUpdateAtColumn.put("typeExpression", "DATETIME");
                        lastUpdateAtColumn.put("charsetName", null);
                        lastUpdateAtColumn.put("position", columns.size() + 1);
                        lastUpdateAtColumn.put("optional", true);
                        lastUpdateAtColumn.put("autoIncremented", false);
                        lastUpdateAtColumn.put("generated", false);
                        lastUpdateAtColumn.put("comment", "this is a comment");
                        lastUpdateAtColumn.put("hasDefaultValue", true);
                        lastUpdateAtColumn.put("enumValues", new JSONArray());

                        columns.add(lastUpdateAtColumn);
                    }

                    LOG.info("rowJson put before: {}", rowJson.toJSONString());
                    rowJson.put("historyRecord", historyRecord.toJSONString());
                    LOG.info("rowJson put after: {}", rowJson.toJSONString());

                    return rowJson.toJSONString();
                }
            }

            /** 同步数据表的数据
             * r: 初始化同步的数据，"ts_ms": 0
             * c: 新增的数据
             * u: 更新的数据
             * d: 删除的数据
             */
            if (Arrays.asList("c", "r", "u").contains(op)) {
                JSONObject after = rowJson.getJSONObject("after");
                after.put("__DORIS_DELETE_SIGN__", 0);
                after.put("last_update_at", lastUpdateAt);
                return rowJson.toJSONString();
            } else if ("d".equals(op)) {
                JSONObject before = rowJson.getJSONObject("before");
                before.put("__DORIS_DELETE_SIGN__", 1);
                before.put("last_update_at", lastUpdateAt);
                return rowJson.toJSONString();
            }

            // 未处理的情况
            LOG.warn("filter other format binlog: {}", row);

            return null;
        });
    }

    private static SingleOutputStreamOperator<String> filterCrud(SingleOutputStreamOperator<String> stream) {
        return stream.filter((FilterFunction<String>) row -> {
            try {
                JSONObject rowJson = JSON.parseObject(row);
                String op = rowJson.getString("op");
                if (op != null && Arrays.asList("c", "r", "u", "d").contains(op)) {
                    return true;
                }
                return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        });
    }

    private static SingleOutputStreamOperator<String> mapToTableRowChangeLog(SingleOutputStreamOperator<String> stream) {
        return stream.map((MapFunction<String, String>) row -> {
            JSONObject rowJson = JSON.parseObject(row);
            String op = rowJson.getString("op");
            JSONObject source = rowJson.getJSONObject("source");
            Long timestamp = source.getLong("ts_ms");
            String lastUpdateAt = timestampToString(timestamp);
            String table = source.getString("table");

            JSONObject changeLog = new JSONObject();
            changeLog.put("__DORIS_DELETE_SIGN__", 0);
            changeLog.put("dt", lastUpdateAt);
            changeLog.put("table_name", table);
            changeLog.put("op", op);
            if (Arrays.asList("c", "r", "u").contains(op)) {
                JSONObject after = rowJson.getJSONObject("after");
                changeLog.put("pk_1", after.getString("id"));
            } else if ("d".equals(op)) {
                JSONObject before = rowJson.getJSONObject("before");
                changeLog.put("pk_1", before.getString("id"));
            }

            return changeLog.toJSONString();
        });
    }

    /**
     * Get all MySQL tables that need to be synchronized
     */
    private static List<String> getTableList() {
        List<String> tables = new ArrayList<>();
        String sql = "SELECT TABLE_SCHEMA,TABLE_NAME FROM information_schema.tables WHERE TABLE_SCHEMA = '" + SYNC_DB + "'";
        List<JSONObject> tableList = JdbcUtil.executeQuery(MYSQL_HOST, MYSQL_PORT, MYSQL_USER, MYSQL_PASSWD, sql);
        for (JSONObject jsob : tableList) {
            String schemaName = jsob.getString("TABLE_SCHEMA");
            String tblName = jsob.getString("TABLE_NAME");
            String schemaTbl = schemaName + "." + tblName;
            if (schemaTbl.matches(SYNC_TBLS)) {
                tables.add(tblName);
            }
        }
        return tables;
    }

    /**
     * create doris sink
     */
    public static DorisSink<String> buildSchemaDorisSink(String table) {
        DorisSink.Builder<String> builder = DorisSink.builder();
        DorisOptions.Builder dorisBuilder = DorisOptions.builder();
        dorisBuilder.setFenodes(FENODES).setBenodes(BENODES)
            .setTableIdentifier(TARGET_DORIS_DB + "." + table)
            .setUsername(DORIS_USER)
            .setPassword(DORIS_PASSWD);

        Properties pro = new Properties();
        //json data format
        pro.setProperty("format", "json");
        pro.setProperty("read_json_by_line", "true");
        DorisExecutionOptions executionOptions = DorisExecutionOptions.builder()
            .setLabelPrefix("label-" + table + UUID.randomUUID()) //streamload label prefix,
            .setStreamLoadProp(pro).setDeletable(true).build();

        builder.setDorisReadOptions(DorisReadOptions.builder().build())
            .setDorisExecutionOptions(executionOptions)
            .setSerializer(JsonDebeziumSchemaSerializer.builder().setDorisOptions(dorisBuilder.build()).build())
            .setDorisOptions(dorisBuilder.build());

        return builder.build();
    }

    public static DorisSink<String> buildSimpleDorisSink(String table) {
        DorisSink.Builder<String> builder = DorisSink.builder();
        DorisOptions.Builder dorisBuilder = DorisOptions.builder();
        dorisBuilder.setFenodes(FENODES).setBenodes(BENODES)
            .setTableIdentifier(TARGET_DORIS_DB + "." + table)
            .setUsername(DORIS_USER)
            .setPassword(DORIS_PASSWD);

        Properties pro = new Properties();
        //json data format
        pro.setProperty("format", "json");
        pro.setProperty("read_json_by_line", "true");
        DorisExecutionOptions executionOptions = DorisExecutionOptions.builder()
            .setLabelPrefix("label-" + table + UUID.randomUUID()) //streamload label prefix,
            .setStreamLoadProp(pro).build();

        builder.setDorisReadOptions(DorisReadOptions.builder().build())
            .setDorisExecutionOptions(executionOptions)
            .setSerializer(new SimpleStringSerializer()) //serialize according to string
            .setDorisOptions(dorisBuilder.build());

        return builder.build();
    }

    private static String timestampToString(Long timestamp) {
        if (timestamp != null) {
//            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Shanghai"));
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return null;
    }
}

package com.fishep.flink.cdc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.doris.flink.cfg.DorisExecutionOptions;
import org.apache.doris.flink.cfg.DorisOptions;
import org.apache.doris.flink.cfg.DorisReadOptions;
import org.apache.doris.flink.sink.DorisSink;
import org.apache.doris.flink.sink.writer.serializer.SimpleStringSerializer;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

/**
 * @Author fly.fei
 * @Date 2024/7/16 18:01
 * @Desc https://github.com/apache/doris/tree/master/samples/doris-demo/flink-demo-v1.1
 **/
@Slf4j
public class KafkaToDoris {

    private static String kafkaServers = "kafka.dev:9092";
    private static String kafkaTopic = "flink-cdc-kafka-doris";
    private static String kafkaGroup = "kafkaGroup";

    private static String fenodes = "doris.dev:8030";
    private static String benodes = "doris.dev:8040";
    private static String database = "demo";
    private static String username = "root";
    private static String password = "";
    private static String TABLE_A = "flink_cdc_kafka_doris";

    private static OutputTag<String> tableA = new OutputTag<String>(TABLE_A) {
    };

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.set(RestOptions.PORT, 8082);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);

        env.setParallelism(1);
        env.enableCheckpointing(10000);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
            .setBootstrapServers(kafkaServers)
            .setTopics(kafkaTopic)
            .setGroupId(kafkaGroup)
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();

        DataStreamSource<String> kafkaDataStreamSource = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        SingleOutputStreamOperator<String> process = kafkaDataStreamSource.process(new ProcessFunction<>() {
            @Override
            public void processElement(String row, ProcessFunction<String, String>.Context context, Collector<String> collector) throws Exception {
                JSONObject rowJson = JSON.parseObject(row);
                String op = rowJson.getString("op");
                JSONObject source = rowJson.getJSONObject("source");
                String table = source.getString("table");
                Long timestamp = source.getLong("ts_ms");
                String lastUpdateAt = timestampToString(timestamp);

                if (Arrays.asList("c", "r", "u").contains(op)) {
                    JSONObject after = rowJson.getJSONObject("after");
                    after.put("__DORIS_DELETE_SIGN__", 0);
                    after.put("last_update_at", lastUpdateAt);
                    context.output(tableA, after.toJSONString());
                } else if ("d".equals(op)) {
                    JSONObject before = rowJson.getJSONObject("before");
                    before.put("__DORIS_DELETE_SIGN__", 1);
                    before.put("last_update_at", lastUpdateAt);
                    context.output(tableA, before.toJSONString());
                }
//                else {
//                    context.output(tableA, );
//                }

                collector.collect(row);
            }
        });

        process.getSideOutput(tableA).sinkTo(buildDorisSink(TABLE_A));

        process.print();

        env.execute("Kafka To Doris");
    }

    public static DorisSink buildDorisSink(String table) {
        DorisSink.Builder<String> builder = DorisSink.builder();
        DorisOptions.Builder dorisBuilder = DorisOptions.builder();
        dorisBuilder.setFenodes(fenodes)
            .setBenodes(benodes)
            .setTableIdentifier(database + "." + table)
            .setUsername(username)
            .setPassword(password);

        Properties pro = new Properties();
        //json data format
        pro.setProperty("format", "json");
        pro.setProperty("read_json_by_line", "true");
        DorisExecutionOptions executionOptions = DorisExecutionOptions.builder()
            .setLabelPrefix("label-" + table) //streamload label prefix,
            .setStreamLoadProp(pro).build();

        builder.setDorisReadOptions(DorisReadOptions.builder().build())
            .setDorisExecutionOptions(executionOptions)
            .setSerializer(new SimpleStringSerializer()) //serialize according to string
            .setDorisOptions(dorisBuilder.build());

        return builder.build();
    }

    private static String timestampToString(Long timestamp) {
        if (timestamp != null) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Shanghai"));
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return null;
    }

}

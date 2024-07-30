package com.fishep.flink.cdc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @Author fly.fei
 * @Date 2024/7/26 17:47
 * @Desc
 **/
@Slf4j
public class ParseAddColumnJson {

    public static void main(String[] args) throws FileNotFoundException {
        FileReader fileReader = new FileReader("src/test/resources/flink/addColumn.json");
//        FileReader fileReader = new FileReader("src/test/resources/flink/addColumn1.json");
        JSONReader jsonReader = new JSONReader(fileReader);
        JSONObject rowObject = JSON.parseObject(jsonReader.readObject().toString());

        String rowJson = rowObject.toJSONString();

        log.info("rowJson: {}", rowJson);

        addColumn(rowJson);
    }

    private static void addColumn(String row) {
        JSONObject rowJson = JSON.parseObject(row);
        String op = rowJson.getString("op");

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

                    log.info("tableChanges before columns: {}", columns);
                    columns.add(lastUpdateAtColumn);
                    log.info("tableChanges after columns: {}", columns);
                }

                log.info("rowJson put before: {}", rowJson.toJSONString());
                rowJson.put("historyRecord", historyRecord.toJSONString());
                log.info("rowJson put after: {}", rowJson.toJSONString());
            }
        }
    }

}

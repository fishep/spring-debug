package com.fishep.calcite;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;

import java.sql.SQLException;

/**
 * @Author fly.fei
 * @Date 2024/8/16 15:02
 * @Desc
 **/
@Slf4j
public class Parse {

    private static String modelPath = "src/test/resources/calcite/model.json";

    public static void main(String[] args) throws SQLException, RelConversionException, ValidationException, SqlParseException {

//        String sql = "SELECT `f`.`id`, ANY_VALUE(`f`.`comment`)\n" +
//            "FROM demo.flink_cdc_kafka_doris AS `f`\n" +
//            "GROUP BY `f`.`id` ";
        String sql = "SELECT f1.id, ANY_VALUE(`f1`.`comment`), ANY_VALUE(`f2`.`comment`) AS f2_comment FROM flink_cdc_1 f1 LEFT JOIN flink_cdc_2 f2 ON (f1.id = f2.id)\n" +
            "WHERE `f2`.`comment` = 'f2' AND customer_functions.IS_CHANGE_TO(`f1`.`comment` , 'f2') \n" +
            "GROUP BY f1.id";

        CalciteConnection connection = CalciteUtils.getConnection(modelPath);
        Planner planner = CalciteUtils.getPlanner(connection);
        SqlNode validatedSqlNode = CalciteUtils.getValidatedSqlNode(planner, sql);
        log.info("validated sql. \n{}", validatedSqlNode.toString());
        RelNode optimizerRelNode = CalciteUtils.getOptimizerRelNode(planner, validatedSqlNode);
        log.info("sql execution plan. \n{}", optimizerRelNode.explain());
    }

}

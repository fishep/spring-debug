package com.fishep.flink;

import com.fishep.spring.debug.jdbc.po.Demo;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @Author fly.fei
 * @Date 2024/5/28 17:13
 * @Desc
 **/
public class MysqlSinkJob {

    private static String hostname = "127.0.0.1";
//    private static String hostname = "alpine";

    private static String url = "jdbc:mysql://mysql.dev:3306/demo";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String username = "demo";
    private static String password = "demo";

    public static void main(String[] args) throws Exception {
        JdbcConnectionOptions jdbcConnectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder().withUrl(url).withDriverName(driver).withUsername(username).withPassword(password).build();

        JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder().withBatchSize(10).withBatchIntervalMs(200).withMaxRetries(3).build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.socketTextStream(hostname, 1024);

        SingleOutputStreamOperator<Demo> map = source.map(s -> {
            Demo demo = new Demo();
            demo.setComment(s);
            return demo;
        });
        
        SinkFunction<Demo> sink = JdbcSink.sink("insert into demo (comment) values (?)", (statement, demo) -> {
            statement.setString(1, demo.getComment());
        }, executionOptions, jdbcConnectionOptions);

        DataStreamSink<Demo> demoDataStreamSink = map.addSink(sink);

        JobExecutionResult result = env.execute("Mysql insert from socket");

//        assertNotNull(result);
    }

}

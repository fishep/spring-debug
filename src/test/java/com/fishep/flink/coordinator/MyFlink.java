package com.fishep.flink.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author fly.fei
 * @Date 2024/8/6 16:14
 * @Desc
 **/
@Slf4j
public class MyFlink {

    private static String hostname = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.set(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);

        env.setParallelism(2);

        DataStreamSource<String> source = env.socketTextStream(hostname, 1024);

        SingleOutputStreamOperator<String> transform = source
            .keyBy(k -> k)
            .transform("transform", TypeInformation.of(String.class), new MyOperatorFactory<>(new MyKeyedProcessOperator<>(new MyKeyedProcessFunction<>())));

        transform.print();

        env.execute("Operator Coordinator Test");
    }

}

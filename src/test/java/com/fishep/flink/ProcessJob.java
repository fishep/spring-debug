package com.fishep.flink;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

/**
 * @Author fly.fei
 * @Date 2024/5/23 14:47
 * @Desc
 **/
public class ProcessJob {

    private static int count = 10;

    public static void main(String[] args) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Integer> source = env.fromData(list);

        SingleOutputStreamOperator<String> process = source.process(new ProcessFunction<Integer, String>() {
            @Override
            public void processElement(Integer value, ProcessFunction<Integer, String>.Context ctx, Collector<String> out) throws Exception {
                if (value % 2 == 0) {
                    out.collect("hello " + value);
                } else {
                    out.collect("world " + value);
                }
            }
        });

        process.print();

        env.execute();
    }

}

package com.fishep.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fly.fei
 * @Date 2024/5/23 14:50
 * @Desc
 **/
public class FilterJob {

    private static int count = 10;

    private static List<Integer> list;

    static {
        list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> source = env.fromData(list);

        SingleOutputStreamOperator<Integer> even = source.filter((FilterFunction<Integer>) value -> value % 2 == 0);
        SingleOutputStreamOperator<Integer> odd = source.filter((FilterFunction<Integer>) value -> value % 2 == 1);

        even.print("even");
        odd.print("odd ");

        source.print("main");

        env.execute();
    }

}

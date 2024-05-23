package com.fishep.flink;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fly.fei
 * @Date 2024/5/23 15:04
 * @Desc
 **/
public class SideJob {

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

        OutputTag<String> left = new OutputTag<>("left", TypeInformation.of(String.class));
        OutputTag<String> right = new OutputTag<>("right", TypeInformation.of(String.class));

        SingleOutputStreamOperator<String> process = source.process(new ProcessFunction<Integer, String>() {
            @Override
            public void processElement(Integer value, ProcessFunction<Integer, String>.Context ctx, Collector<String> out) throws Exception {
                if (value % 3 == 0) {
                    ctx.output(left, "left " + value);
                } else if (value % 3 == 2) {
                    ctx.output(right, "right " + value);
                } else {
                    out.collect("main " + value);
                }
            }
        });

        SideOutputDataStream<String> leftOutput = process.getSideOutput(left);
        SideOutputDataStream<String> rightOutput = process.getSideOutput(right);

        leftOutput.print("left ");
        process.print("main ");
        rightOutput.print("right ");

        env.execute();
    }

}

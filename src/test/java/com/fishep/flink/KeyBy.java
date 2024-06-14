package com.fishep.flink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @Author fly.fei
 * @Date 2024/6/14 11:52
 * @Desc
 **/
@Slf4j
public class KeyBy {

    private static List<Tuple2<Long, String>> stream;

    static {
        stream = new LinkedList<>();
        stream.add(Tuple2.of(0L, "name0"));
        stream.add(Tuple2.of(1L, "name1-0"));
        stream.add(Tuple2.of(1L, "name1-1"));
        stream.add(Tuple2.of(2L, "name2-0"));
        stream.add(Tuple2.of(2L, "name2-1"));
        stream.add(Tuple2.of(2L, "name2-2"));
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);

        DataStreamSource<Tuple2<Long, String>> source = env.fromData(stream);

        source.keyBy(t -> t.f0)
            .process(new KeyedProcessFunction<Long, Tuple2<Long, String>, Tuple2<Long, String>>() {

                private int count;

                @Override
                public void processElement(Tuple2<Long, String> value, KeyedProcessFunction<Long, Tuple2<Long, String>, Tuple2<Long, String>>.Context ctx, Collector<Tuple2<Long, String>> out) throws Exception {

//                    相同key的数据一定在同一个线程执行, 不同key的数据可能在同一个线程执行
//                    相同线程使用相同this对象，不同线程使用不同this对象

                    log.info("id: " + value.f0 + ", name: " + value.f1);
                    log.info("this: " + this.hashCode());
                    log.info("count: " + count++);

                    out.collect(value);
                }
            })
            .print();

        env.execute();
    }

}

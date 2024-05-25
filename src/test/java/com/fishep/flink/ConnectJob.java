package com.fishep.flink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * @Author fly.fei
 * @Date 2024/5/24 9:46
 * @Desc
 **/
@Slf4j
public class ConnectJob {

    private static List<Tuple2<Long, String>> table2;

    private static List<Tuple3<Long, String, String>> table3;

    static {
        table2 = new LinkedList<>();
        table2.add(Tuple2.of(0L, "name0"));
        table2.add(Tuple2.of(1L, "name1"));
        table2.add(Tuple2.of(2L, "name2"));
        table2.add(Tuple2.of(4L, "name4-0"));
        table2.add(Tuple2.of(4L, "name4-1"));

        table3 = new LinkedList<>();
        table3.add(Tuple3.of(1L, "record1", "this is table3 record1"));
        table3.add(Tuple3.of(2L, "record2-0", "this is table3 record2-0"));
        table3.add(Tuple3.of(2L, "record2-1", "this is table3 record2-1"));
        table3.add(Tuple3.of(4L, "record4-0", "this is table3 record4-0"));
        table3.add(Tuple3.of(4L, "record4-1", "this is table3 record4-1"));
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(3);

        DataStreamSource<Tuple2<Long, String>> source1 = env.fromData(table2);
        DataStreamSource<Tuple3<Long, String, String>> source2 = env.fromData(table3);

        ConnectedStreams<Tuple2<Long, String>, Tuple3<Long, String, String>> streams = source1.connect(source2);

        ConnectedStreams<Tuple2<Long, String>, Tuple3<Long, String, String>> keyBy = streams.keyBy((KeySelector<Tuple2<Long, String>, Long>) value -> value.f0, (KeySelector<Tuple3<Long, String, String>, Long>) value -> value.f0);

        SingleOutputStreamOperator<String> process = keyBy.process(new CoProcessFunction<>() {
            //            private static Map<Long, List<Tuple2<Long, String>>> map2 = new ConcurrentHashMap<>();
//            private static Map<Long, List<Tuple3<Long, String, String>>> map3 = new ConcurrentHashMap<>();
            private Map<Long, List<Tuple2<Long, String>>> map2 = new HashMap<>();
            private Map<Long, List<Tuple3<Long, String, String>>> map3 = new HashMap<>();

            @Override
            public void processElement1(Tuple2<Long, String> value, CoProcessFunction<Tuple2<Long, String>, Tuple3<Long, String, String>, String>.Context ctx, Collector<String> out) throws Exception {
//                log.info("processElement1");
//                log.info(map2.toString());

                if (!map2.containsKey(value.f0)) {
                    map2.put(value.f0, new ArrayList<>());
                }
                map2.get(value.f0).add(value);

                if (map3.containsKey(value.f0)) {
                    for (Tuple3<Long, String, String> t : map3.get(value.f0)) {
                        out.collect(value + " ---> " + t);
                    }
                }
            }

            @Override
            public void processElement2(Tuple3<Long, String, String> value, CoProcessFunction<Tuple2<Long, String>, Tuple3<Long, String, String>, String>.Context ctx, Collector<String> out) throws Exception {
//                log.info("processElement2");
//                log.info(map3.toString());

                if (!map3.containsKey(value.f0)) {
                    map3.put(value.f0, new ArrayList<>());
                }
                map3.get(value.f0).add(value);

                if (map2.containsKey(value.f0)) {
                    for (Tuple2<Long, String> t : map2.get(value.f0)) {
                        out.collect(t + " ---> " + value);
                    }
                }
            }
        });

        process.print();

        env.execute();
    }

}

package com.fishep.flink;

import com.fishep.testfixture.flink.WordCount;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author fly.fei
 * @Date 2024/5/14 15:50
 * @Desc
 **/
@Slf4j
public class SocketSource {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env.socketTextStream("127.0.0.1", 1024);

        SingleOutputStreamOperator<String> map = source.map(s -> {
            log.info(s);
            return s;
        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMap = map.flatMap(new WordCount());

        KeyedStream<Tuple2<String, Integer>, String> keyedStream = flatMap.keyBy(t -> t.f0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);

        DataStreamSink<Tuple2<String, Integer>> print = sum.print();

        JobExecutionResult result = env.execute("WordCount from socket");

        assertNotNull(result);
    }

}

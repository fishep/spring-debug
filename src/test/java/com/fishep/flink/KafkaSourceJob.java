package com.fishep.flink;

import com.fishep.testfixture.flink.WordCount;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author fly.fei
 * @Date 2024/5/20 18:10
 * @Desc
 **/
@Slf4j
public class KafkaSourceJob {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
            .setBootstrapServers("kafka.dev:9092")
            .setTopics("topic-demo")
            .setGroupId("my-group")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();

        DataStreamSource<String> source = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");

        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMap = source.flatMap(new WordCount());

        KeyedStream<Tuple2<String, Integer>, String> keyedStream = flatMap.keyBy(t -> t.f0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);

        DataStreamSink<Tuple2<String, Integer>> print = sum.print();

        JobExecutionResult result = env.execute("WordCount from socket");

//        assertNotNull(result);
    }

}

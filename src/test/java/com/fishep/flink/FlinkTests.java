package com.fishep.flink;

import com.fishep.testfixture.flink.WordCount;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class FlinkTests {

    private static String filePath = "src/test/resources/flink/flink.txt";

    @Test
    void hello() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> stream = env.fromData("world", "flink", "spring");
        SingleOutputStreamOperator<String> map = stream.map(s -> "hello " + s);
        map.print();

        env.execute("Hello Flink Job");
    }

    @Test
    @Deprecated
    void dataSet() throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> source = env.readTextFile(filePath);
        FlatMapOperator<String, Tuple2<String, Integer>> map = source.flatMap(new WordCount()).returns(new TypeHint<>() {
        });
        UnsortedGrouping<Tuple2<String, Integer>> group = map.groupBy(0);
        AggregateOperator<Tuple2<String, Integer>> sum = group.sum(1);
        sum.print();
    }

    @Test
    void fileSource() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        FileSource<String> fileSource = FileSource.forRecordStreamFormat(new TextLineInputFormat(), new Path(filePath)).build();

        DataStreamSource<String> source = env.fromSource(fileSource, WatermarkStrategy.noWatermarks(), "file");

        SingleOutputStreamOperator<Tuple2<String, Integer>> map = source.flatMap(new WordCount());

        KeyedStream<Tuple2<String, Integer>, String> keyedStream = map.keyBy(t -> t.f0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);

        DataStreamSink<Tuple2<String, Integer>> print = sum.print();

        JobExecutionResult result = env.execute("WordCount from file");

        assertNotNull(result);
    }

}

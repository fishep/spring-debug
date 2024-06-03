package com.fishep.flink;

import com.fishep.testfixture.flink.WaterSensor;
import com.fishep.testfixture.flink.WaterSensorMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @Author fly.fei
 * @Date 2024/5/14 15:50
 * @Desc
 **/
@Slf4j
public class JoinJob {

    private static String hostname = "127.0.0.1";
//    private static String hostname = "alpine";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> source1 = env.socketTextStream(hostname, 1024).map(new WaterSensorMap());
        SingleOutputStreamOperator<WaterSensor> source2 = env.socketTextStream(hostname, 1025).map(new WaterSensorMap());

        WatermarkStrategy<WaterSensor> strategy = WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.of(3L, SECONDS))
            .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000);

        SingleOutputStreamOperator<WaterSensor> stream1 = source1.assignTimestampsAndWatermarks(strategy);
        SingleOutputStreamOperator<WaterSensor> stream2 = source2.assignTimestampsAndWatermarks(strategy);

        KeyedStream<WaterSensor, String> keyBy1 = stream1.keyBy(WaterSensor::getId);
        KeyedStream<WaterSensor, String> keyBy2 = stream2.keyBy(WaterSensor::getId);

        DataStream<String> process = keyBy1.join(keyBy2)
            .where((KeySelector<WaterSensor, String>) WaterSensor::getId)
            .equalTo((KeySelector<WaterSensor, String>) WaterSensor::getId)
            .window(TumblingEventTimeWindows.of(Duration.of(10L, SECONDS)))
            .apply((JoinFunction<WaterSensor, WaterSensor, String>) (first, second) -> first + " <---> " + second);

        DataStreamSink<String> print = process.print();

        env.execute("join from socket");
    }

}

package com.fishep.flink;

import com.fishep.testfixture.flink.WaterSensor;
import com.fishep.testfixture.flink.WaterSensorMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @Author fly.fei
 * @Date 2024/5/14 15:50
 * @Desc
 **/
@Slf4j
public class IntervalJoin {

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

        OutputTag<WaterSensor> leftLateOutputTag = new OutputTag<>("LeftLateOutputTag", Types.POJO(WaterSensor.class));
        OutputTag<WaterSensor> rightLateOutputTag = new OutputTag<>("RightLateOutputTag", Types.POJO(WaterSensor.class));

        SingleOutputStreamOperator<String> process = keyBy1.intervalJoin(keyBy2)
            .between(Duration.of(-2L, SECONDS), Duration.of(2L, SECONDS))
            .sideOutputLeftLateData(leftLateOutputTag)
            .sideOutputRightLateData(rightLateOutputTag)
            .process(new ProcessJoinFunction<>() {
                @Override
                public void processElement(WaterSensor left, WaterSensor right, ProcessJoinFunction<WaterSensor, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
                    out.collect(left + " <---> " + right);
                }
            });

        process.getSideOutput(leftLateOutputTag).printToErr("LeftLateOutputTag");
        process.getSideOutput(rightLateOutputTag).printToErr("RightLateOutputTag");
        DataStreamSink<String> print = process.print("MainStream");

        env.execute("intervalJoin from socket");
    }

}

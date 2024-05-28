package com.fishep.flink;

import com.fishep.testfixture.flink.WaterSensor;
import com.fishep.testfixture.flink.WaterSensorMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @Author fly.fei
 * @Date 2024/5/14 15:50
 * @Desc
 **/
@Slf4j
public class OutOfOrderJob {

    private static String hostname = "127.0.0.1";
//    private static String hostname = "alpine";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        env.disableOperatorChaining();

        DataStreamSource<String> source = env.socketTextStream(hostname, 1024);

        SingleOutputStreamOperator<WaterSensor> map = source.map(new WaterSensorMap());

        WatermarkStrategy<WaterSensor> strategy = WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.of(3L, SECONDS))
            .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000);

//        SingleOutputStreamOperator<WaterSensor> stream = map.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.of(10L, SECONDS)));
        SingleOutputStreamOperator<WaterSensor> stream = map.assignTimestampsAndWatermarks(strategy);

        KeyedStream<WaterSensor, String> keyBy = stream.keyBy(WaterSensor::getId);

        WindowedStream<WaterSensor, String, TimeWindow> window = keyBy.window(TumblingEventTimeWindows.of(Duration.of(10L, SECONDS)));
//        WindowedStream<WaterSensor, String, TimeWindow> window = keyBy.window(TumblingProcessingTimeWindows.of(Duration.of(10L, SECONDS)));

//        window.aggregate()

        SingleOutputStreamOperator<String> process = window.process(new ProcessWindowFunction<>() {
            @Override
            public void open(OpenContext openContext) throws Exception {
                super.open(openContext);

                log.info("open: " + openContext);
            }

            @Override
            public void process(String s, ProcessWindowFunction<WaterSensor, String, String, TimeWindow>.Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                log.info("window start time: " + context.window().getStart());
                log.info("window end time: " + context.window().getEnd());
                log.info("processing Time: " + context.currentProcessingTime());
                log.info("elements: " + elements);
//s1,1,1
                out.collect(elements.toString());
            }

            @Override
            public void close() throws Exception {
                super.close();

                log.info("close");
            }
        }, Types.STRING);

        DataStreamSink<String> print = process.print();

        env.execute("OutOfOrderJob from socket");
    }


}

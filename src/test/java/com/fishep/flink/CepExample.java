package com.fishep.flink;

import com.fishep.testfixture.flink.cep.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @Author fly.fei
 * @Date 2024/6/6 18:09
 * @Desc
 **/
public class CepExample {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<LoginEvent> stream = env
            .fromData(
                new LoginEvent("user_1", "0.0.0.0", "fail", 2000L),
                new LoginEvent("user_1", "0.0.0.1", "fail", 3000L),
                new LoginEvent("user_1", "0.0.0.2", "success", 4000L),
                new LoginEvent("user_1", "0.0.0.3", "fail", 5000L)
            )
            .assignTimestampsAndWatermarks(
                WatermarkStrategy
                    .<LoginEvent>forMonotonousTimestamps()
                    .withTimestampAssigner((SerializableTimestampAssigner<LoginEvent>) (loginEvent, l) -> loginEvent.eventTime)
            )
            .keyBy(r -> r.userId);

        SimpleCondition<LoginEvent> simpleCondition = new SimpleCondition<>() {
            @Override
            public boolean filter(LoginEvent loginEvent) throws Exception {
                return loginEvent.eventType.equals("fail");
            }
        };

        Pattern<LoginEvent, LoginEvent> pattern = Pattern
            .<LoginEvent>begin("first")
            .where(simpleCondition)
            .next("second")
            .where(simpleCondition)
            .followedBy("third")
            .where(simpleCondition)
            .within(Duration.of(5L, SECONDS));

        PatternStream<LoginEvent> patternedStream = CEP.pattern(stream, pattern);

        patternedStream
            .select(new PatternSelectFunction<LoginEvent, Tuple4<String, String, String, String>>(){
                @Override
                public Tuple4<String, String, String, String> select(Map<String, List<LoginEvent>> pattern) throws Exception {
                    LoginEvent first = pattern.get("first").iterator().next();
                    LoginEvent second = pattern.get("second").iterator().next();
                    LoginEvent third = pattern.get("third").iterator().next();
                    return Tuple4.of(first.userId, first.ipAddress, second.ipAddress, third.ipAddress);
                }
            })
            .print("fail 3 times");

        env.execute();
    }

}

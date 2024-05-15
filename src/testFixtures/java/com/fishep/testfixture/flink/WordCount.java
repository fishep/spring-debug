package com.fishep.testfixture.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @Author fly.fei
 * @Date 2024/5/13 12:20
 * @Desc
 **/
public class WordCount implements FlatMapFunction<String, Tuple2<String, Integer>> {

    @Override
    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
        String[] words = value.split(" ");
        for (String word : words) {
            out.collect(new Tuple2<>(word, 1));
        }
    }

}

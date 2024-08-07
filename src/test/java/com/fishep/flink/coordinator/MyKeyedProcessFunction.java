package com.fishep.flink.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @Author fly.fei
 * @Date 2024/8/6 12:24
 * @Desc
 **/
@Slf4j
public class MyKeyedProcessFunction<K, I, O> extends KeyedProcessFunction<K, I, O> {
    @Override
    public void processElement(I value, KeyedProcessFunction<K, I, O>.Context ctx, Collector<O> out) throws Exception {
        log.info("MyKeyedProcessFunction processElement this: " + this);
    }

    @Override
    public void open(OpenContext openContext) throws Exception {
        super.open(openContext);

        log.info("MyKeyedProcessFunction open this: " + this);
    }

    public void handleOperatorEvent(OperatorEvent evt) {
        log.info("MyKeyedProcessFunction handleOperatorEvent this: " + this);
        log.info("MyKeyedProcessFunction handleOperatorEvent evt: " + evt.toString());
    }

}

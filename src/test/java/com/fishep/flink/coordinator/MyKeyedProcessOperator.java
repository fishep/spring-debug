package com.fishep.flink.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;
import org.apache.flink.runtime.operators.coordination.OperatorEventHandler;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.operators.KeyedProcessOperator;

/**
 * @Author fly.fei
 * @Date 2024/8/6 12:13
 * @Desc
 **/
@Slf4j
public class MyKeyedProcessOperator<K, IN, OUT> extends KeyedProcessOperator<K, IN, OUT> implements OperatorEventHandler {

    protected MyKeyedProcessFunction<K, IN, OUT> processFunction;

    public MyKeyedProcessOperator(MyKeyedProcessFunction<K, IN, OUT> userFunction) {
        super(userFunction);

        processFunction = userFunction;

        log.info("MyKeyedProcessOperator MyKeyedProcessOperator");
    }

    @Override
    public void handleOperatorEvent(OperatorEvent evt) {
        log.info("MyKeyedProcessOperator handleOperatorEvent this: " + this);
        log.info("MyKeyedProcessOperator handleOperatorEvent evt: " + evt.toString());

        processFunction.handleOperatorEvent(evt);
    }

    @Override
    public boolean hasKeyContext() {

        log.info("MyKeyedProcessOperator hasKeyContext");

        return super.hasKeyContext();
    }

}

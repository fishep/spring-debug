package com.fishep.flink.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.streaming.api.operators.*;

/**
 * @Author fly.fei
 * @Date 2024/8/6 12:17
 * @Desc
 **/
@Slf4j
public class MyOperatorFactory<K, IN, OUT> extends SimpleUdfStreamOperatorFactory<OUT>
    implements OneInputStreamOperatorFactory<IN, OUT>, CoordinatedOperatorFactory<OUT> {

    protected MyKeyedProcessOperator<K, IN, OUT> operator;

    public MyOperatorFactory(MyKeyedProcessOperator<K, IN, OUT> myKeyedProcessOperator) {
        super(myKeyedProcessOperator);
        this.operator = myKeyedProcessOperator;

        log.info("MyOperatorFactory MyOperatorFactory");
    }

    @Override
    public <T extends StreamOperator<OUT>> T createStreamOperator(StreamOperatorParameters<OUT> parameters) {
        log.info("MyOperatorFactory createStreamOperator");

        T streamOperator = super.createStreamOperator(parameters);

        assert streamOperator == operator : "MyOperatorFactory operator is not same";

        parameters.getOperatorEventDispatcher().registerEventHandler(operator.getOperatorID(), operator);

        return streamOperator;
    }

    @Override
    public OperatorCoordinator.Provider getCoordinatorProvider(String operatorName, OperatorID operatorID) {

        log.info("MyOperatorFactory getCoordinatorProvider");

        return new MyOperatorCoordinatorProvider(operatorID);
    }
}

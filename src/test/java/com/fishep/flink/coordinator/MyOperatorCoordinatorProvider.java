package com.fishep.flink.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.runtime.operators.coordination.RecreateOnResetOperatorCoordinator;

/**
 * @Author fly.fei
 * @Date 2024/8/6 14:13
 * @Desc
 **/
@Slf4j
public class MyOperatorCoordinatorProvider extends RecreateOnResetOperatorCoordinator.Provider {

    public MyOperatorCoordinatorProvider(OperatorID operatorID) {
        super(operatorID);

        log.info("MyOperatorCoordinatorProvider MyOperatorCoordinatorProvider");
    }

    @Override
    protected OperatorCoordinator getCoordinator(OperatorCoordinator.Context context) throws Exception {

        log.info("MyOperatorCoordinatorProvider getCoordinator");

        return new MyOperatorCoordinator();
    }
}

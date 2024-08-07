package com.fishep.flink.coordinator;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

/**
 * @Author fly.fei
 * @Date 2024/8/6 14:12
 * @Desc
 **/
@Slf4j
public class MyOperatorCoordinator implements OperatorCoordinator {

    @Override
    public void start() throws Exception {
        log.info("MyOperatorCoordinator start");
    }

    @Override
    public void close() throws Exception {
        log.info("MyOperatorCoordinator close");
    }

    @Override
    public void handleEventFromOperator(int subtask, int attemptNumber, OperatorEvent event) throws Exception {
        log.info("MyOperatorCoordinator handleEventFromOperator");
    }

    @Override
    public void checkpointCoordinator(long checkpointId, CompletableFuture<byte[]> resultFuture) throws Exception {
        log.info("MyOperatorCoordinator checkpointCoordinator");
    }

    @Override
    public void notifyCheckpointComplete(long checkpointId) {
        log.info("MyOperatorCoordinator notifyCheckpointComplete");
    }

    @Override
    public void resetToCheckpoint(long checkpointId, @Nullable byte[] checkpointData) throws Exception {
        log.info("MyOperatorCoordinator resetToCheckpoint");
    }

    @Override
    public void subtaskReset(int subtask, long checkpointId) {
        log.info("MyOperatorCoordinator subtaskReset");
    }

    @Override
    public void executionAttemptFailed(int subtask, int attemptNumber, @Nullable Throwable reason) {
        log.info("MyOperatorCoordinator executionAttemptFailed");
    }

    @Override
    public void executionAttemptReady(int subtask, int attemptNumber, SubtaskGateway gateway) {
//        每个子任务执行一次
        log.info("MyOperatorCoordinator executionAttemptReady");

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);

                    log.info("MyOperatorCoordinator executionAttemptReady sendEvent");
                    gateway.sendEvent(new MyOperatorEvent());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}

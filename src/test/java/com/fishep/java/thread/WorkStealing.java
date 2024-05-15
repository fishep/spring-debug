package com.fishep.java.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author fly.fei
 * @Date 2024/5/15 11:55
 * @Desc
 **/
@Slf4j
public class WorkStealing {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newWorkStealingPool();
        for (int i = 0; i < 10; i++) {
            es.submit(() -> {

                log.info("stealing");

                return 0;
            });
        }

        log.info("main");

        new CountDownLatch(1).await();
    }

}

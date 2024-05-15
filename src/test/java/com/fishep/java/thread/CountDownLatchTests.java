package com.fishep.java.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/14 16:47
 * @Desc
 **/
@Slf4j
public class CountDownLatchTests {

    @Test
    void countDownLatchZero() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(0);
//        CountDownLatch latch = new CountDownLatch(1);

        latch.await();

        log.info("goodbye world !");
    }

    @Test
    void countDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                log.info("Count: " + countDownLatch.getCount());
                countDownLatch.countDown();
            });
            thread.start();
        }
        countDownLatch.await();

        log.info("goodbye world !");
    }

}

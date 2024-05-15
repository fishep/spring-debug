package com.fishep.java.thread;

import com.fishep.testfixture.thread.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author fly.fei
 * @Date 2024/5/15 12:00
 * @Desc
 **/
@Slf4j
public class ScheduledThreadPool {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
        ses.schedule(new Task("one-time"), 1, TimeUnit.SECONDS);
        ses.scheduleAtFixedRate(new Task("fixed-rate"), 0, 2, TimeUnit.SECONDS);
        ses.scheduleWithFixedDelay(new Task("fixed-delay"), 1, 3, TimeUnit.SECONDS);

//        ses.awaitTermination(60L, TimeUnit.SECONDS);

        new CountDownLatch(1).await();
    }

}

package com.fishep.java.thread;

import com.fishep.testfixture.thread.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author fly.fei
 * @Date 2024/5/15 12:07
 * @Desc
 **/
@Slf4j
public class SingleThreadExecutor {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 3; i++) {
            es.submit(new Task("task" + i));
        }

//        es.awaitTermination(4L, TimeUnit.SECONDS);

        new CountDownLatch(1).await();
    }

}

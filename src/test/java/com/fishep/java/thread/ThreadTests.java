package com.fishep.java.thread;

import com.fishep.testfixture.thread.SumTask;
import com.fishep.testfixture.thread.Task;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author fly.fei
 * @Date 2024/4/16 11:07
 * @Desc
 **/
@Slf4j
public class ThreadTests {

    @Test
    void cachedThreadPool() {
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("task" + i));
        }

        es.shutdown();
    }

    @Test
    void future() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("Callable call");
                return "hello world";
            }
        });

        String s = future.get();

        log.info(s);

        es.shutdown();
    }

    @Test
    void completableFuture() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return "hello world";
        }, es);

//        TimeUnit.SECONDS.sleep(1L);

        future.thenAccept((s) -> {
            log.info("thenAccept");
        });

        String s = future.get();

        log.info(s);
    }

    @Test
    void forkJoinTask() {
        long[] array = new long[2000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
            expectedSum += array[i];
        }

        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);

        Long result = task.invoke();
//        Long result = ForkJoinPool.commonPool().invoke(task);
//        Long result = new ForkJoinPool().invoke(task);

        assertEquals(expectedSum, result);
    }

}

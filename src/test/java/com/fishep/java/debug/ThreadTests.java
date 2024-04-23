package com.fishep.java.debug;

import com.fishep.java.debug.thread.SumTask;
import com.fishep.java.debug.thread.Task;
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
    void threadPoolExecutor() throws InterruptedException {
        int min = 1;
        int max = 2;
//        ExecutorService es = new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        ExecutorService es = new ThreadPoolExecutor(min, max, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 3; i++) {
            es.submit(new Task("task" + i));
        }
//        es.shutdown();
        es.awaitTermination(4L, TimeUnit.SECONDS);
    }

    @Test
    void fixedThreadPool() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("task" + i));
        }

        es.awaitTermination(2L, TimeUnit.SECONDS);
    }


    @Test
    void cachedThreadPool() {
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("task" + i));
        }

        es.shutdown();
    }

    @Test
    void singleThreadExecutor() throws InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 3; i++) {
            es.submit(new Task("task" + i));
        }

        es.awaitTermination(4L, TimeUnit.SECONDS);
    }

    @Test
    void scheduledThreadPool() throws InterruptedException {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
        ses.schedule(new Task("one-time"), 1, TimeUnit.SECONDS);
        ses.scheduleAtFixedRate(new Task("fixed-rate"), 1, 2, TimeUnit.SECONDS);
        ses.scheduleWithFixedDelay(new Task("fixed-delay"), 1, 3, TimeUnit.SECONDS);

        ses.awaitTermination(30L, TimeUnit.SECONDS);
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

    @Test
    void workStealing() throws InterruptedException {
        ExecutorService es = Executors.newWorkStealingPool();
        for (int i = 0; i < 10; i++) {
            es.submit(() -> {
                Thread.sleep(1000);

                log.info("stealing");

                return 0;
            });
        }

        log.info("main");

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void timer() throws InterruptedException {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.trace("task1 start");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.trace("task1 end");
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.trace("task2 start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.trace("task2 end");
            }
        };

        timer.scheduleAtFixedRate(task1, 0, 3000);
        timer.scheduleAtFixedRate(task2, 0, 5000);

        TimeUnit.SECONDS.sleep(100);
    }

}

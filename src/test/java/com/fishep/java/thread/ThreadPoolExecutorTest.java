package com.fishep.java.thread;

import com.fishep.testfixture.thread.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author fly.fei
 * @Date 2024/5/15 12:11
 * @Desc
 **/
@Slf4j
public class ThreadPoolExecutorTest {

    private static Integer count = 100;

    private static Integer capacity = 1;

    public static void main(String[] args) throws InterruptedException {
        int min = 2;
        int max = 8;
//        ExecutorService es = new ThreadPoolExecutor(min, max, 0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
//        ExecutorService es = new ThreadPoolExecutor(min, max, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        ExecutorService es = new ThreadPoolExecutor(min, max, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(capacity));

        for (int i = 0; i < count; i++) {
            log.info("submit task " + i);

//            if the task cannot be scheduled for execution
//            队列被填满之后，再提交task就会异常， 生产者的生产速度 - 消费者的消费速度 > capacity
            es.submit(new Task("task" + i));
        }
//        es.shutdown();
//        es.awaitTermination(4L, TimeUnit.SECONDS);

        new CountDownLatch(1).await();
    }

}

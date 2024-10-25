package com.fishep.java.network.reactor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author fly.fei
 * @Date 2024/10/25 12:18
 * @Desc
 **/
public class ExecutorServiceSingleton {

    private static Integer min = 1;

    private static Integer max = 10;

    private static Integer capacity = 1;

    private static ExecutorServiceSingleton executorServiceSingleton;

    private ExecutorService es = new ThreadPoolExecutor(min, max, 60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(capacity));

    private ExecutorServiceSingleton() {
    }

    public static ExecutorServiceSingleton instance() {
        if (executorServiceSingleton == null) {
            synchronized (ExecutorServiceSingleton.class) {
                if (executorServiceSingleton == null) {
                    executorServiceSingleton = new ExecutorServiceSingleton();
                }
            }
        }

        return executorServiceSingleton;
    }

    public void submit(Runnable task) {
        es.submit(task);
    }

}

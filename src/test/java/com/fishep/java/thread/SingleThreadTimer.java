package com.fishep.java.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/15 11:45
 * @Desc
 **/
@Slf4j
public class SingleThreadTimer {

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.info("task1 start");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("task1 end");
            }
        };

        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.info("task2 start");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("task2 end");
            }
        };

        timer.scheduleAtFixedRate(task1, 0, 3000);
        timer.scheduleAtFixedRate(task2, 0, 5000);

        new CountDownLatch(1).await();
    }

}

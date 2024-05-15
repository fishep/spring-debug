package com.fishep.testfixture.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author fly.fei
 * @Date 2024/4/16 11:09
 * @Desc
 **/
@Slf4j
public class Task implements Runnable {

    private final String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        log.trace("run() start");

        log.trace("name: " + name);

//        try {
//            if (name.equals("fixed-rate")) {
//                throw new RuntimeException("err");
//            }
//            TimeUnit.SECONDS.sleep(1L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        log.trace("run() end");
    }

}

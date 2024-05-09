package com.fishep.testfixture.thread;

/**
 * @Author fly.fei
 * @Date 2024/4/16 11:09
 * @Desc
 **/
public class Task implements Runnable {

    private final String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

//            if (name.equals("fixed-rate")){
//                throw new RuntimeException("err");
//            }

        } catch (InterruptedException e) {
        }
    }

}

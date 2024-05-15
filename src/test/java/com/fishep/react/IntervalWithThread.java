package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/15 11:25
 * @Desc
 **/
@Slf4j
public class IntervalWithThread {

    public static void main(String[] args) throws InterruptedException {
        log.info("main thread start");

        Thread thread = new Thread(() -> {
            log.info("thread start");

            String s = "hello ";

            Flux.interval(Duration.ofSeconds(1)).map(input -> s + "interval " + input).subscribe(log::info);

//            s = "world";

            log.info("thread end");
        });
        thread.start();

        thread.join();

        log.info("main thread end");

        new CountDownLatch(1).await();
    }

}

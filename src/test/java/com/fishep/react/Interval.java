package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/14 16:33
 * @Desc
 **/
@Slf4j
public class Interval {

    private static Integer count = 10;

    public static void main(String[] args) throws InterruptedException {
        log.info("before interval");

        Disposable subscribe = Flux.interval(Duration.ofSeconds(1)).map(input -> {
            if (input < count) return "tick " + input;
            throw new RuntimeException("boom");
        }).onErrorReturn("Uh oh").subscribe(log::info);

//        subscribe.dispose();

        log.info("end interval");

        new CountDownLatch(1).await();
    }

}

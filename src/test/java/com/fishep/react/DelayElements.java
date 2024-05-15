package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/15 9:43
 * @Desc
 **/
@Slf4j
public class DelayElements {

    private static Integer count = 10;

    public static void main(String[] args) throws InterruptedException {

        Flux.range(0, count).map(Object::toString).delayElements(Duration.ofSeconds(1)).subscribe(log::info);

        new CountDownLatch(1).await();
    }

}

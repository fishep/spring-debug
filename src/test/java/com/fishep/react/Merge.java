package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/15 11:18
 * @Desc
 **/
@Slf4j
public class Merge {

    public static void main(String[] args) throws InterruptedException {
        Flux<String> flux1 = Flux.interval(Duration.ofMillis(300)).map(x -> "p1: " + x);
        Flux<String> flux2 = Flux.interval(Duration.ofMillis(500)).map(x -> "p2: " + x);
        Flux<String> mergeFlux = Flux.merge(flux1, flux2);

        mergeFlux.subscribe(log::info);

        new CountDownLatch(1).await();
    }

}

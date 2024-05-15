package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Author fly.fei
 * @Date 2024/5/15 11:20
 * @Desc
 **/
@Slf4j
public class Concat {

    public static void main(String[] args) throws InterruptedException {
        Flux<String> flux1 = Flux.just("1", "2", "3").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("4", "5", "6", "7");

        Flux<String> flux = Flux.concat(flux1, flux2);

        flux.subscribe(log::info);

        new CountDownLatch(1).await();
    }

}

package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * @Author fly.fei
 * @Date 2024/5/15 11:14
 * @Desc
 **/
@Slf4j
public class ConcatMap {

    private static Integer count = 3;

    public static void main(String[] args) throws InterruptedException {

        Function<Integer, Publisher<String>> mapper = t -> switch (t) {
            case 2 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "tue " + i);
            case 1 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "mon " + i);
            case 3 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "wed " + i);
            case 4 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "thu " + i);
            case 5 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "fri " + i);
            case 6 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "sat " + i);
            case 7 -> Flux.interval(Duration.ofSeconds(1L)).take(count).map(i -> "sun " + i);
            default -> Flux.empty();
        };

        Flux.just(1, 2, 3, 4, 5, 6, 7).concatMap(mapper).subscribe(log::info);

        new CountDownLatch(1).await();
    }

}

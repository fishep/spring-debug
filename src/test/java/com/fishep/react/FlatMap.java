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
 * @Date 2024/5/15 9:57
 * @Desc
 **/
@Slf4j
public class FlatMap {

    private static Integer count = 10;

    public static void main(String[] args) throws InterruptedException {

        Function<Integer, Publisher<String>> mapper = t -> switch (t) {
            case 1 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "mon " + i);
            case 2 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "tue " + i);
            case 3 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "wed " + i);
            case 4 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "thu " + i);
            case 5 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "fri " + i);
            case 6 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "sat " + i);
            case 7 -> Flux.interval(Duration.ofSeconds(ThreadLocalRandom.current().nextInt(count))).take(count).map(i -> "sun " + i);
            default -> Flux.empty();
        };

        Flux.just(1, 2, 3, 4, 5, 6, 7).flatMap(mapper).subscribe(log::info);

        new CountDownLatch(1).await();
    }

}

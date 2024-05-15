package com.fishep.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author fly.fei
 * @Date 2024/5/14 17:32
 * @Desc
 **/
@Slf4j
public class RefCount {

    public static void main(String[] args) throws InterruptedException {
        Flux<String> source = Flux.interval(Duration.ofMillis(500)).map(Object::toString).doOnSubscribe(s -> log.info("doOnSubscribe")).doOnCancel(() -> log.info("doOnCancel"));

        Flux<String> flux = source.publish().refCount(2, Duration.ofSeconds(2));

        log.info("subscribed first");
        Disposable s1 = flux.subscribe(x -> log.info("s1:" + x));

        TimeUnit.SECONDS.sleep(1);
        log.info("subscribed second");
        Disposable s2 = flux.subscribe(x -> log.info("s2:" + x));

        TimeUnit.SECONDS.sleep(1);
        log.info("subscribed first disposable");
        s1.dispose();

        TimeUnit.SECONDS.sleep(1);
        log.info("subscribed second disposable"); // 所有的订阅者都取消了
        s2.dispose();

        TimeUnit.SECONDS.sleep(1); // 在2s内 s3进行了订阅
        log.info("subscribed third");
        Disposable s3 = flux.subscribe(x -> log.info("s3:" + x));
        log.info("subscribed third end");

        TimeUnit.SECONDS.sleep(1);
        log.info("subscribed third disposable");
        s3.dispose(); // 所有订阅者都取消了 disconnect

        TimeUnit.SECONDS.sleep(3);
        log.info("subscribed fourth"); // 3s 后（超过了2s）s4、s5订阅，触发connect
        Disposable sub4 = flux.subscribe(l -> log.info("s4: " + l));
        TimeUnit.SECONDS.sleep(1);
        log.info("subscribed  fifth");
        Disposable sub5 = flux.subscribe(l -> log.info("s5: " + l));

        new CountDownLatch(1).await();
    }

}

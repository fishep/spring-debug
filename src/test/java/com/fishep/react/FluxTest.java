package com.fishep.react;

import com.fishep.testfixture.react.MyEvent;
import com.fishep.testfixture.react.MyEventListener;
import com.fishep.testfixture.react.MyEventProcessor;
import com.fishep.testfixture.react.MySubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @Author fly.fei
 * @Date 2024/4/11 11:22
 * @Desc
 **/
@Slf4j
public class FluxTest {

    @Test
    void just() {
        String data = "key";
        Flux.just(data).map(k -> "val").filter(v -> v.equals("val")).subscribe(new MySubscriber<>());
    }

    @Test
    void empty() {
        Flux.empty().subscribe(t -> log.info("do"), t -> log.info("error"), () -> log.info("complete"));
    }

    @Test
    void error() {
        Flux.error(new RuntimeException("emitter an error")).subscribe(t -> log.info("do"), t -> log.info("error"), () -> log.info("complete"));
    }

    @Test
    void range() {
        Flux.range(3, 3).subscribe(t -> log.info("int " + t));
    }

    @Test
    void interval() throws InterruptedException {
        log.trace("before interval");

        Flux.interval(Duration.ofSeconds(1)).map(input -> {
            if (input < 3) return "tick " + input;
            throw new RuntimeException("boom");
        }).onErrorReturn("Uh oh").subscribe(log::info);

        TimeUnit.SECONDS.sleep(5L);

        log.trace("end interval");
    }

    @Test
    void generate() {
        Flux<String> flux = Flux.generate(() -> 0, (state, sink) -> {
            sink.next("3 x " + state + " = " + 3 * state);
            if (state == 3) {
                sink.complete();
            }
            return state + 1;
        }, (state) -> log.info("state: " + state));

        flux.subscribe(log::info);
    }

    @Test
    void create() throws InterruptedException {
        MyEventProcessor<String> myEventProcesser = new MyEventProcessor<>();
        Flux.create(emitter -> {
            myEventProcesser.register(new MyEventListener<>() {
                @Override
                public void onDataChunk(MyEvent<String> event) {
                    emitter.next(event);
                }

                @Override
                public void processComplete() {
                    emitter.complete();
                }
            });

            emitter.onRequest(n -> {
                log.info("initialRequestConsumer");
            });
        }).subscribe(t -> log.info(t.toString()), t -> log.info("error"), () -> log.info("complete"));

        for (int i = 0; i < 3; i++) {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000));
            myEventProcesser.newEvent(new MyEvent<>(new Date(), "Event" + i));
        }
        myEventProcesser.processComplete();
    }

    @Test
    void subscribe() {
        Flux.just("hello")
            .doOnSubscribe(s -> log.info("doOnSubscribe"))
            .doOnComplete(() -> log.info("doOnComplete"))
            .doOnError(s -> log.info("doOnError"))
            .subscribe();

        Flux.error(new RuntimeException("emitter an error"))
            .doOnSubscribe(s -> log.info("doOnSubscribe"))
            .doOnComplete(() -> log.info("doOnComplete"))
            .doOnError(s -> log.info("doOnError"))
            .subscribe();
    }

    @Test
    void doOnSubscribe() throws InterruptedException {
        Flux<Integer> flux1 = Flux.range(1, 3).doOnSubscribe(s -> log.info("flux1 doOnSubscribe"));
        Flux<String> flux2 = flux1.map(Object::toString).doOnSubscribe(s -> log.info("flux2 doOnSubscribe"));
        ConnectableFlux<String> flux3 = flux2.publish();

        Flux<String> flux4 = flux3.doOnSubscribe(s -> log.info("flux4 doOnSubscribe"));
        flux3.subscribe(log::info);
        flux3.connect();

        flux4.subscribe(log::info);
        flux3.connect();
    }

    @Test
    void connect() throws InterruptedException {
        Flux<String> source = Flux.range(1, 3).map(Object::toString).doOnSubscribe(s -> log.info("subscribed to source"));

        ConnectableFlux<String> co = source.publish();

        co.subscribe(log::info);
        co.subscribe(log::info);

        log.info("done subscribing");
        TimeUnit.SECONDS.sleep(1);

        log.info("will now connect");
        co.connect();
    }

    @Test
    void refCount() throws InterruptedException {
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
        TimeUnit.SECONDS.sleep(2);

    }

    @Test
    void defer() throws InterruptedException {
        Flux<String> flux1 = Flux.just(new Date().toString());
        Flux<String> flux2 = Flux.defer(() -> Flux.just(new Date().toString()));

        flux1.subscribe(x -> log.info("s1: " + x));
        flux2.subscribe(x -> log.info("s2: " + x));

        TimeUnit.SECONDS.sleep(3);

        flux1.subscribe(x -> log.info("s3: " + x));
        flux2.subscribe(x -> log.info("s4: " + x));
    }

    @Test
    void delayElements() throws InterruptedException {
        Flux.just("1", "2").delayElements(Duration.ofSeconds(2)).subscribe(log::info);

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    void map() {
        Flux.just(1, 2, 3, 4).map(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return i * 2 + "";
        }).log().subscribe(log::info);
    }

    @Test
    void flatMap() throws InterruptedException {
//        Function<Integer, Publisher<String>> mapper = i -> Flux.just(i * 2 + "");
        Function<Integer, Publisher<String>> mapper = i -> Flux.just(i * 2 + "").delayElements(Duration.ofSeconds(1));

        Flux.just(1, 2, 3, 4).flatMap(mapper).subscribe(log::info);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void flatMap2() throws InterruptedException {
        Function<Integer, Publisher<String>> mapper = t -> switch (t) {
            case 1 -> Flux.interval(Duration.ofSeconds(1L)).take(5).map(i -> "mon" + i);
            case 2 -> Flux.interval(Duration.ofSeconds(1L)).take(5).map(i -> "tue" + i);
            case 3 -> Flux.interval(Duration.ofSeconds(1L)).take(5).map(i -> "wed" + i);
            case 4 -> Flux.interval(Duration.ofSeconds(1L)).take(5).map(i -> "thu" + i);
            default -> Flux.empty();
        };

        Flux.just(1, 2, 3, 4).flatMap(mapper).subscribe(log::info);

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    void flatMap3() {
        Flux<Integer> board = Flux.range(1, 8);
        Flux<String> row = board.map(String::valueOf);
        Flux<String> column = board.map(x -> 'a' + x - 1).map(Character::toString);

        Flux<String> squares = row.flatMap(r -> column.map(c -> c + r));

        AtomicInteger i = new AtomicInteger();
        squares.subscribe(t -> {
            if (i.get() % 8 == 0) {
                System.out.println();
            }
            i.getAndIncrement();
            System.out.print(t + " ");
        });
    }

    @Test
    void concatMap() throws InterruptedException {
        Function<Integer, Publisher<String>> mapper = t -> switch (t) {
            case 1 -> Flux.interval(Duration.ofSeconds(1L)).take(2).map(i -> "mon" + i);
            case 2 -> Flux.interval(Duration.ofSeconds(1L)).take(2).map(i -> "tue" + i);
            case 3 -> Flux.interval(Duration.ofSeconds(1L)).take(2).map(i -> "wed" + i);
            default -> Flux.empty();
        };

        Flux.just(1, 2, 3, 4).concatMap(mapper).subscribe(log::info);

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    void merge() throws InterruptedException {
        Flux<String> flux1 = Flux.interval(Duration.ofMillis(300)).map(x -> "p1: " + x);
        Flux<String> flux2 = Flux.interval(Duration.ofMillis(500)).map(x -> "p2: " + x);
        Flux<String> mergeFlux = Flux.merge(flux1, flux2);

        mergeFlux.subscribe(log::info);

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void zip() {
        Flux<Tuple2<String, String>> zip = Flux.zip(Flux.just("hello"), Flux.just("world"));
        zip.subscribe(t -> log.info(t.getT1() + " " + t.getT2()));
    }

    @Test
    void concat() throws InterruptedException {
        Flux<String> flux1 = Flux.just("1", "2", "3").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("4", "5", "6");

        Flux<String> flux = Flux.concat(flux1, flux2);

        flux.subscribe(log::info);

        TimeUnit.SECONDS.sleep(4);
    }

    @Test
    void threadExitWithInterval() throws InterruptedException {

        Thread thread = new Thread(() -> {
            String s = "hello ";

            log.trace("before interval");
            Flux.interval(Duration.ofSeconds(1)).map(input -> s + "interval " + input).subscribe(log::info);
            log.trace("end interval");

            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            s = "world";

            log.trace("thread end");
        });
        thread.start();

        thread.join();

        log.trace("thread end");

        TimeUnit.SECONDS.sleep(5);
    }

}

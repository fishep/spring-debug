package com.fishep.react;

import com.fishep.testfixture.react.MyEvent;
import com.fishep.testfixture.react.MyEventListener;
import com.fishep.testfixture.react.MyEventProcessor;
import com.fishep.testfixture.react.MySubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @Author fly.fei
 * @Date 2024/4/11 11:22
 * @Desc
 **/
@Slf4j
public class FluxTests {

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
//            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1000));
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

        log.info("done subscribing");

        co.connect();
    }

    @Test
    void defer() {
        Flux<String> flux1 = Flux.just(new Date().toString());
        Flux<String> flux2 = Flux.defer(() -> Flux.just(new Date().toString()));

        flux1.subscribe(x1 -> {
            log.info("x1: " + x1);
            flux1.subscribe(x2 -> {
                log.info("x2: " + x2);
                assertSame(x1, x2);
            });
        });

        flux2.subscribe(x1 -> {
            log.info("x1: " + x1);
            flux1.subscribe(x2 -> {
                log.info("x2: " + x2);
                assertNotSame(x1, x2);
            });
        });
    }

    @Test
    void map() {
        Flux.just(1, 2, 3, 4).map(i -> i * 2 + "").log().subscribe(log::info);
    }

    @Test
    void flatMap() throws InterruptedException {
        Function<Integer, Publisher<String>> mapper = i -> Flux.just(i * 2 + "");
//        Function<Integer, Publisher<String>> mapper = i -> Flux.just(i * 2 + "").delayElements(Duration.ofSeconds(1));

        Flux.just(1, 2, 3, 4).flatMap(mapper).subscribe(log::info);

//        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void flatMapCartesian() {
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
    void zip() {
        Flux<Tuple2<String, String>> zip = Flux.zip(Flux.just("hello"), Flux.just("world"));
        zip.subscribe(t -> log.info(t.getT1() + " " + t.getT2()));
    }

}

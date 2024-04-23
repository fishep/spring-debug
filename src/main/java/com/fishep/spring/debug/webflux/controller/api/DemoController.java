package com.fishep.spring.debug.webflux.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @Author fly.fei
 * @Date 2024/4/18 10:20
 * @Desc
 **/
@org.springframework.web.bind.annotation.RestController("apiDemoController")
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        log.info("hello");

        return Mono.just("hello !");
    }

    @GetMapping("/helloWorld")
    public Flux<String> helloWorld() {
        log.info("helloWorld");

        ArrayList<String> list = new ArrayList<>();
        list.add("hello ");
        list.add("world !");

        return Flux.fromIterable(list);
    }

    @GetMapping("/interval")
    public Flux<String> interval() {
        log.trace("before interval");

        Flux<String> flux = Flux.interval(Duration.ofSeconds(1)).map(input -> {
                if (input < 3) return "interval " + input + "\n";
                throw new RuntimeException("boom");
//                return "interval " + input + "\n";
            })
            .doOnSubscribe(t -> log.info("doOnSubscribe " + t.toString()))
            .onErrorReturn("boom ...");

        log.trace("end interval");

        return flux;
    }

}

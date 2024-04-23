package com.fishep.spring.debug.webflux.controller.tpl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @Author fly.fei
 * @Date 2024/4/18 10:22
 * @Desc
 **/
@Controller("tplDemoController")
@RequestMapping("/tpl/demo")
public class DemoController {

    @GetMapping("/index")
    public Mono<String> index(Model model) {
        model.addAttribute("data", "index thymeleaf !");
//        return "index";
        return Mono.just("index");
    }

    @GetMapping("/hello")
    public Mono<String> hello(Model model) {
        model.addAttribute("data", "hello thymeleaf !");
//        return "demo/hello";
        return Mono.just("demo/hello");
    }

    @GetMapping("/helloWorld")
    public Mono<Rendering> helloWorld(Model model) {
        model.addAttribute("data", "hello thymeleaf !");

        Flux<String> flux = Flux.interval(Duration.ofSeconds(1)).map(input -> {
            if (input < 3) return "interval " + input + "\n";
            throw new RuntimeException("boom");
//                return "interval " + input + "\n";
        }).onErrorReturn("boom ...");

        return Mono.just(Rendering.view("demo/helloWorld").modelAttribute("flux", flux).build());
    }

}
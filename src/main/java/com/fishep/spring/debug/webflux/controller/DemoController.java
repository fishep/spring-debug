package com.fishep.spring.debug.webflux.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

/**
 * @Author fly.fei
 * @Date 2024/4/18 10:22
 * @Desc
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/hello")
    public Mono<String> hello() {
//        return "hello.html";
        return Mono.just("hello.html");
    }

}
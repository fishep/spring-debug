package com.fishep.spring.debug.web.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author fly.fei
 * @Date 2024/4/18 10:20
 * @Desc
 **/
@org.springframework.web.bind.annotation.RestController("apiDemoController")
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world!";
    }

}

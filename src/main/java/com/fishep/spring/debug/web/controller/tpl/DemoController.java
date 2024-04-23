package com.fishep.spring.debug.web.controller.tpl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author fly.fei
 * @Date 2024/4/18 10:22
 * @Desc
 **/
@Controller("tplDemoController")
@RequestMapping("/tpl/demo")
public class DemoController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("data", "index thymeleaf !");
        return "index";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello thymeleaf !");
        return "demo/hello";
    }

}
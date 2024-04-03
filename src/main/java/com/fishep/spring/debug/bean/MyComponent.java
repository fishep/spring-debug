package com.fishep.spring.debug.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/2/29 10:29
 * @Desc
 **/
@Slf4j
@Component
public class MyComponent {

    public String method1() {
        return "method1";
    }

    public void initMethod() {

        log.trace("initMethod begin");

        log.trace("initMethod end");

    }

}

package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * @Author fly.fei
 * @Date 2023/12/7 11:56
 * @Desc
 **/
@Slf4j
public class MyApplicationListener<T extends ApplicationEvent> implements org.springframework.context.ApplicationListener<T> {

    @Override
    public void onApplicationEvent(T event) {
        log.trace("onApplicationEvent begin");

        log.trace("event: " + event.getClass().getSimpleName());

        log.trace("onApplicationEvent end");
    }

}

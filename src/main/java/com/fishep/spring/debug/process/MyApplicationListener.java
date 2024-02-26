package com.fishep.spring.debug.process;

import org.springframework.context.ApplicationEvent;

/**
 * @Author fly.fei
 * @Date 2023/12/7 11:56
 * @Desc
 **/
public class MyApplicationListener implements org.springframework.context.ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("------------MyApplicationListener");
        System.out.println(event);
        System.out.println("------------MyApplicationListener end");
    }
}

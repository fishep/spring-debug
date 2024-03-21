package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * @Author fly.fei
 * @Date 2023/12/7 16:52
 * @Desc
 **/
@Slf4j
public class MySpringApplicationRunListener implements SpringApplicationRunListener, Ordered {

    public MySpringApplicationRunListener(SpringApplication application, String[] args) {
        log.trace("MySpringApplicationRunListener begin");
        log.trace("MySpringApplicationRunListener end");
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        log.trace("starting begin");
        log.trace("starting end");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        log.trace("environmentPrepared begin");
        log.trace("environmentPrepared end");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.trace("contextPrepared begin");
        log.trace("contextPrepared end");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.trace("contextLoaded begin");
        log.trace("contextLoaded end");
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        log.trace("started begin");
        log.trace("started end");
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        log.trace("ready begin");
        log.trace("ready end");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.trace("failed begin");
        log.trace("failed end");
    }

}

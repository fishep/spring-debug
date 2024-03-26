package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:05
 * @Desc
 **/
@Slf4j
public class MyApplicationContextFactory implements ApplicationContextFactory {

    @Override
    public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {

        log.trace("create begin");

        log.trace("create end");

        return null;
    }

    @Override
    public Class<? extends ConfigurableEnvironment> getEnvironmentType(WebApplicationType webApplicationType) {

        log.trace("getEnvironmentType begin");

        log.trace("getEnvironmentType end");

        return ApplicationContextFactory.super.getEnvironmentType(webApplicationType);
    }

    @Override
    public ConfigurableEnvironment createEnvironment(WebApplicationType webApplicationType) {

        log.trace("createEnvironment begin");

        log.trace("createEnvironment end");

        return ApplicationContextFactory.super.createEnvironment(webApplicationType);
    }
}

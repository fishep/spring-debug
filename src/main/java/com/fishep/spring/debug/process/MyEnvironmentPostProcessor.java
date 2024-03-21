package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author fly.fei
 * @Date 2023/12/11 15:41
 * @Desc
 **/
@Slf4j
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.trace("postProcessEnvironment begin");

        log.trace("postProcessEnvironment end");
    }
}

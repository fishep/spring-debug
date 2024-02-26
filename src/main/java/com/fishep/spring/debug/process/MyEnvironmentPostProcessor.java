package com.fishep.spring.debug.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author fly.fei
 * @Date 2023/12/11 15:41
 * @Desc
 **/
public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("------------MyEnvironmentPostProcessor");
        System.out.println(environment);
        System.out.println(application);
        System.out.println("------------MyEnvironmentPostProcessor end");
    }
}

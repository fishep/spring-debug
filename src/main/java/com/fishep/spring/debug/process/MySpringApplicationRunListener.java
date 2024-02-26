package com.fishep.spring.debug.process;

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
public class MySpringApplicationRunListener implements SpringApplicationRunListener, Ordered {

    public MySpringApplicationRunListener(SpringApplication application, String[] args) {
        System.out.println("------------MySpringApplicationRunListener ");
        System.out.println(application);
        System.out.println(args);
        System.out.println("------------MySpringApplicationRunListener end");
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println("------------MySpringApplicationRunListener starting ");
        System.out.println(bootstrapContext);
        System.out.println("------------MySpringApplicationRunListener starting end");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println("------------MySpringApplicationRunListener environmentPrepared ");
        System.out.println(bootstrapContext);
        System.out.println(environment);
        System.out.println("------------MySpringApplicationRunListener environmentPrepared end");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("------------MySpringApplicationRunListener contextPrepared ");
        System.out.println(context);
        System.out.println("------------MySpringApplicationRunListener contextPrepared end");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("------------MySpringApplicationRunListener contextLoaded ");
        System.out.println(context);
        System.out.println("------------MySpringApplicationRunListener contextLoaded end");
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("------------MySpringApplicationRunListener started ");
        System.out.println(context);
        System.out.println(timeTaken);
        System.out.println("------------MySpringApplicationRunListener started end");
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("------------MySpringApplicationRunListener ready ");
        System.out.println(context);
        System.out.println(timeTaken);
        System.out.println("------------MySpringApplicationRunListener ready end");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("------------MySpringApplicationRunListener failed ");
        System.out.println(context);
        System.out.println(exception);
        System.out.println("------------MySpringApplicationRunListener failed end");
    }
}

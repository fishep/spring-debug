package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * @Author fly.fei
 * @Date 2023/12/7 11:55
 * @Desc
 **/
@Slf4j
public class MyBootstrapRegistryInitializer implements BootstrapRegistryInitializer {
    @Override
    public void initialize(BootstrapRegistry registry) {
        log.trace("initialize begin");

        registry.register(MyRegister.class, context -> {

            log.trace("MyRegister instance begin");
            log.trace("MyRegister instance end");

            return new MyRegister();
        });

        log.trace("initialize end");
    }
}

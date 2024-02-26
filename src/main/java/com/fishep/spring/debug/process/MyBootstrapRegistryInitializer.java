package com.fishep.spring.debug.process;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * @Author fly.fei
 * @Date 2023/12/7 11:55
 * @Desc
 **/
public class MyBootstrapRegistryInitializer implements BootstrapRegistryInitializer {
    @Override
    public void initialize(BootstrapRegistry registry) {

        System.out.println("------------MyBootstrapRegistryInitializer");
        System.out.println(registry);
        System.out.println("------------MyBootstrapRegistryInitializer end");

        registry.register(MyRegister.class, context -> {
            System.out.println("------------MyBootstrapRegistryInitializer context ");
            System.out.println(context);
            System.out.println("------------MyBootstrapRegistryInitializer context end");
            return new MyRegister();
        });
    }
}

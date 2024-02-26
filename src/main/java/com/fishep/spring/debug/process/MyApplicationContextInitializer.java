package com.fishep.spring.debug.process;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.PriorityOrdered;

/**
 * @Author fly.fei
 * @Date 2023/12/7 11:56
 * @Desc
 **/
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("------------MyApplicationContextInitializer");
        System.out.println(applicationContext);
        System.out.println("------------MyApplicationContextInitializer end");

        MyBeanDefinitionRegistryPostProcessor postProcessor = new MyBeanDefinitionRegistryPostProcessor();
        applicationContext.addBeanFactoryPostProcessor(postProcessor);
    }

    static class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            System.out.println("------------MyBeanDefinitionRegistryPostProcessor postProcessBeanDefinitionRegistry");
            System.out.println(registry);
            System.out.println("------------MyBeanDefinitionRegistryPostProcessor postProcessBeanDefinitionRegistry end");
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("------------MyBeanDefinitionRegistryPostProcessor postProcessBeanFactory");
            System.out.println(beanFactory);
            System.out.println("------------MyBeanDefinitionRegistryPostProcessor postProcessBeanFactory end");
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}

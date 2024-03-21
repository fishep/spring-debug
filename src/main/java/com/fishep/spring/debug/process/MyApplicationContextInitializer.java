package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.trace("initialize begin");

        MyBeanDefinitionRegistryPostProcessor postProcessor = new MyBeanDefinitionRegistryPostProcessor();
        applicationContext.addBeanFactoryPostProcessor(postProcessor);

        log.trace("initialize end");
    }

    static class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            log.trace("postProcessBeanDefinitionRegistry begin");
            log.trace("postProcessBeanDefinitionRegistry end");
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            log.trace("postProcessBeanFactory begin");
            log.trace("postProcessBeanFactory end");
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}

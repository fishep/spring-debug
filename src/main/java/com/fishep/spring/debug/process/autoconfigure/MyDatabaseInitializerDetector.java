package com.fishep.spring.debug.process.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.sql.init.dependency.DatabaseInitializerDetector;

import java.util.Set;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:50
 * @Desc
 **/
@Slf4j
public class MyDatabaseInitializerDetector implements DatabaseInitializerDetector {

    @Override
    public Set<String> detect(ConfigurableListableBeanFactory beanFactory) {

        log.trace("detect begin");

        log.trace("detect end");

        return null;
    }

    @Override
    public void detectionComplete(ConfigurableListableBeanFactory beanFactory, Set<String> dataSourceInitializerNames) {

        log.trace("detectionComplete begin");

        log.trace("detectionComplete end");

        DatabaseInitializerDetector.super.detectionComplete(beanFactory, dataSourceInitializerNames);
    }

    @Override
    public int getOrder() {

        log.trace("getOrder begin");

        log.trace("getOrder end");

        return DatabaseInitializerDetector.super.getOrder();
    }
}

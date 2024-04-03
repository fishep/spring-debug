package com.fishep.spring.debug.process.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.sql.init.dependency.AbstractBeansOfTypeDatabaseInitializerDetector;

import java.util.Collections;
import java.util.Set;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:50
 * @Desc
 **/
@Slf4j
public class MyDatabaseInitializerDetector extends AbstractBeansOfTypeDatabaseInitializerDetector {

    @Override
    protected Set<Class<?>> getDatabaseInitializerBeanTypes() {

        log.trace("getDatabaseInitializerBeanTypes begin");

        log.trace("getDatabaseInitializerBeanTypes end");

        return Collections.emptySet();
    }

}

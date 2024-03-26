package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.LoggingSystemFactory;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:02
 * @Desc
 **/
@Slf4j
public class MyLoggingSystemFactory implements LoggingSystemFactory {

    @Override
    public LoggingSystem getLoggingSystem(ClassLoader classLoader) {

        log.trace("getLoggingSystem begin");

        log.trace("getLoggingSystem end");

        return null;
    }

}

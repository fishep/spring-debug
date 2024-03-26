package com.fishep.spring.debug.process.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:37
 * @Desc
 **/
@Slf4j
public class OnMyCondition implements AutoConfigurationImportFilter {

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {

        log.trace("match begin");

        log.trace("match end");

        return new boolean[0];
    }

}

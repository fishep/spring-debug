package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.*;

import java.io.IOException;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:24
 * @Desc
 **/
@Slf4j
public class MyConfigDataLoader implements ConfigDataLoader<StandardConfigDataResource> {

    @Override
    public ConfigData load(ConfigDataLoaderContext context, StandardConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {

        log.trace("load begin");

        log.trace("load end");

        return null;
    }

    @Override
    public boolean isLoadable(ConfigDataLoaderContext context, StandardConfigDataResource resource) {

        log.trace("isLoadable begin");

        log.trace("isLoadable end");

        return ConfigDataLoader.super.isLoadable(context, resource);
    }
}

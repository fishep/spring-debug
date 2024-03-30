package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import java.io.IOException;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:24
 * @Desc
 **/
@Slf4j
public class MyConfigDataLoader implements ConfigDataLoader<MyConfigDataResource> {

    @Override
    public boolean isLoadable(ConfigDataLoaderContext context, MyConfigDataResource resource) {

        log.trace("load begin");

        log.trace("load end");

        return ConfigDataLoader.super.isLoadable(context, resource);
    }

    @Override
    public ConfigData load(ConfigDataLoaderContext context, MyConfigDataResource resource) throws IOException, ConfigDataResourceNotFoundException {

        log.trace("isLoadable begin");

        log.trace("isLoadable end");

        return null;
    }

}

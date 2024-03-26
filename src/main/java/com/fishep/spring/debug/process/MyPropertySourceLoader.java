package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:21
 * @Desc
 **/
@Slf4j
public class MyPropertySourceLoader implements PropertySourceLoader {

    @Override
    public String[] getFileExtensions() {

        log.trace("getFileExtensions begin");

        log.trace("getFileExtensions end");

        return new String[0];
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {

        log.trace("load begin");

        log.trace("load end");

        return null;
    }

}

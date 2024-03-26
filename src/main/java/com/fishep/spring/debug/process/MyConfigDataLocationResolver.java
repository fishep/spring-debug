package com.fishep.spring.debug.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.*;

import java.util.List;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:16
 * @Desc
 **/
@Slf4j
public class MyConfigDataLocationResolver implements ConfigDataLocationResolver<StandardConfigDataResource> {

    @Override
    public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {

        log.trace("isResolvable begin");

        log.trace("isResolvable end");

        return false;
    }

    @Override
    public List<StandardConfigDataResource> resolve(ConfigDataLocationResolverContext context, ConfigDataLocation location) throws ConfigDataLocationNotFoundException, ConfigDataResourceNotFoundException {

        log.trace("resolve begin");

        log.trace("resolve end");

        return null;
    }

    @Override
    public List<StandardConfigDataResource> resolveProfileSpecific(ConfigDataLocationResolverContext context, ConfigDataLocation location, Profiles profiles) throws ConfigDataLocationNotFoundException {

        log.trace("resolveProfileSpecific begin");

        log.trace("resolveProfileSpecific end");

        return ConfigDataLocationResolver.super.resolveProfileSpecific(context, location, profiles);
    }

}

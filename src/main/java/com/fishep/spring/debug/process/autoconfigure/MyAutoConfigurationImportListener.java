package com.fishep.spring.debug.process.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;

/**
 * @Author fly.fei
 * @Date 2024/3/26 17:48
 * @Desc
 **/
@Slf4j
public class MyAutoConfigurationImportListener implements AutoConfigurationImportListener {

    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {

        log.trace("onAutoConfigurationImportEvent begin");

        log.trace("onAutoConfigurationImportEvent end");

    }

}

package com.fishep.spring.debug.bean;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @Author fly.fei
 * @Date 2024/2/29 10:48
 * @Desc
 **/
@Slf4j
@SpringBootTest
class AutowiredComponentTest {

    @Autowired
    private AutowiredComponent autowiredComponent;

    @Test
    void autowiredComponent() {
        log.info("autowiredComponent begin");

        assertNotNull(autowiredComponent.getMyComponent());
        assertNull(autowiredComponent.getValue());

        log.info("autowiredComponent end");
    }

}
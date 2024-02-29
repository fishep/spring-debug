package com.fishep.spring.debug.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/2/29 10:48
 * @Desc
 **/
@SpringBootTest
class AutowiredComponentTest {

    @Autowired
    private AutowiredComponent autowiredComponent;

    @Test
    void autowiredComponent() {
        assertNotNull(autowiredComponent.getMyComponent());
        assertNull(autowiredComponent.getValue());
    }
}
package com.fishep.java.debug;

import com.fishep.java.debug.generic.Generic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @Author fly.fei
 * @Date 2024/3/13 11:52
 * @Desc
 **/
public class GenericSingletonTests {

    @Test
    void singleton(){
        Generic<Object> instance = Generic.getInstance();
        instance.setT("hello");
        assertEquals("hello", instance.getT());
        assertInstanceOf(String.class, instance.getT());

        instance.setT(100L);
        assertEquals(100L, instance.getT());
        assertInstanceOf(Long.class, instance.getT());

        Generic.print(GenericSingletonTests.class);
    }

}

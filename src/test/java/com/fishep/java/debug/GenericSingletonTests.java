package com.fishep.java.debug;

import com.fishep.java.debug.generic.GenericSingleton;
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
        GenericSingleton<Object> instance = GenericSingleton.getInstance();
        instance.setT("hello");
        assertEquals("hello", instance.getT());
        assertInstanceOf(String.class, instance.getT());

        instance.setT(100L);
        assertEquals(100L, instance.getT());
        assertInstanceOf(Long.class, instance.getT());

        GenericSingleton.print(GenericSingletonTests.class);
    }

}

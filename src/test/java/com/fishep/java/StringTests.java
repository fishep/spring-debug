package com.fishep.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/3/21 9:41
 * @Desc
 **/
public class StringTests {

    @Test
    void sameOrNotSame() {
        String key1 = "hello";
        String key2 = "hello";
        String key3 = new String("hello");
        String key4 = new String("he") + new String("llo");
        String key5 = "he" + "llo";

        assertSame(key1, key2);
        assertNotSame(key1, key3);
        assertNotSame(key1, key4);
        assertNotSame(key3, key4);
        assertSame(key1, key5);

        String intern3 = key3.intern();
        String intern4 = key4.intern();

        assertSame(key1, intern3);
        assertSame(key1, intern4);
    }

}

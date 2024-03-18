package com.fishep.java.debug;

import com.fishep.java.debug.inherit.Children;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/3/18 16:11
 * @Desc
 **/
public class ConcurrentHashMapTests {

    @Test
    void stringHashMap() {
        ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();

        String key1 = "hello";
        String key2 = "hello";
        String key3 = new String("hello");

        assertSame(key1, key2);
        assertNotSame(key1, key3);

        hashMap.put(key1, "world");
        assertEquals("world", hashMap.get(key2));
        assertEquals("world", hashMap.get(key3));
    }

    @Test
    void objectHashMap() {
        ConcurrentHashMap<Object, Object> hashMap = new ConcurrentHashMap<>();

        Children children1 = new Children();
        Children children2 = new Children();

        assertNotSame(children1, children2);

        hashMap.put(children1, "world");
        assertEquals("world", hashMap.get(children1));
        assertNull(hashMap.get(children2));
    }
}

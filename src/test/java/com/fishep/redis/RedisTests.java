package com.fishep.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/5/24 17:13
 * @Desc
 **/
@Slf4j
public class RedisTests {

    private static RedisClient client;
    private static StatefulRedisConnection<String, String> connect;

    private static String key = "key";
    private static String val = "value";

    @BeforeAll
    static void beforeAll() {
        client = RedisClient.create("redis://redis.dev/");
        connect = client.connect();
    }

    @AfterAll
    static void afterAll() {
        if (connect != null) {
            connect.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Test
    void set() {
        RedisCommands<String, String> sync = connect.sync();
        String ret = sync.set(key, val);

        assertEquals("OK", ret);
        assertEquals(val, sync.get(key));

        sync.del(key);
    }

    @Test
    void setnx() {
        RedisCommands<String, String> sync = connect.sync();

        assertTrue(sync.setnx(key, val));
        assertFalse(sync.setnx(key, val));

        sync.del(key);
    }

}

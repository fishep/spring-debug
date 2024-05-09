package com.fishep.spring.debug.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class RedisTemplateTests {

    @Autowired
    private StringRedisTemplate redisTemplate;
//    private RedisTemplate<String, String> redisTemplate;

    @Test
    void set() {
        String key = "key";
        String val = "value";
        redisTemplate.opsForValue().set(key, val);

        assertEquals(val, redisTemplate.opsForValue().get(key));
    }

}

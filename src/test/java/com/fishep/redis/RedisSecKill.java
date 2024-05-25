package com.fishep.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author fly.fei
 * @Date 2024/5/24 17:38
 * @Desc
 **/
@Slf4j
public class RedisSecKill {

    private static RedisClient client;
    private static StatefulRedisConnection<String, String> connect;

    private static String key = "secKillLimit";
    private static String val = "300";
    private static Integer count = 1000;

    private static void beforeAll() {
        client = RedisClient.create("redis://redis.dev/");
        connect = client.connect();
    }

    private static void afterAll() {
        if (connect != null) {
            connect.close();
        }
        if (client != null) {
            client.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        beforeAll();

        RedisCommands<String, String> sync = connect.sync();
        String ret = sync.set(key, val);
        assertEquals("OK", ret);

        AtomicLong success = new AtomicLong();// 成功的个数

        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                Long decr = sync.decr(key);
                if (decr >= 0) {

                    // 模拟业务
                    try {
                        int n = new Random().nextInt(10);
                        if (n == 1) {
                            throw new RuntimeException();
                        }
                        log.info("success");
                        success.incrementAndGet();
                    } catch (RuntimeException e) {
                        sync.incr(key);
                        log.info("exception");
                    }

                } else {
                    sync.incr(key);
                    log.info("fail");
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(3);

        assertEquals("0", sync.get(key));
        assertEquals(Long.valueOf(val), success.get());

        sync.del(key);

        afterAll();
    }

}

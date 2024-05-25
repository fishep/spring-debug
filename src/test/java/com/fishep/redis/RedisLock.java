package com.fishep.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author fly.fei
 * @Date 2024/5/24 17:38
 * @Desc
 **/
@Slf4j
public class RedisLock {

    private static RedisClient client;
    private static StatefulRedisConnection<String, String> connect;

    private static String key = "key";
    private static String val = "value";

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

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                boolean flag = false;
                try {
                    flag = RedisUtil.lock(key, val);

                    log.info("lock status: " + flag);

                    if (flag) {
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (flag) {
                        RedisUtil.unlock(key);
                    }
                }
            }).start();

            TimeUnit.SECONDS.sleep(1);
        }

        new CountDownLatch(1).await();

        afterAll();
    }


    public class RedisUtil {

        public static boolean lock(String key, String val) {
            return connect.sync().setnx(key, val);
        }

        public static boolean unlock(String key) {
            return connect.sync().del(key) == 1;
        }

    }

}

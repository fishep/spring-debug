package com.fishep.redis;

import com.fishep.testfixture.redis.MyKey;
import com.fishep.testfixture.redis.MyValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author fly.fei
 * @Date 2024/5/25 9:45
 * @Desc
 **/
@Slf4j
public class RedissonTests {

    private static RedissonClient redisson;

    @BeforeAll
    static void beforeAll() {
        Config config = new Config();
//        config.useClusterServers().addNodeAddress("redis://redis.dev:6379/");
        config.useSingleServer().setAddress("redis://redis.dev:6379/");
// or read config from file
//        config = Config.fromYAML(new File("config-file.yaml"));
        redisson = Redisson.create(config);
    }

    @AfterAll
    static void afterAll() {
        if (redisson != null) {
            redisson.shutdown();
        }
    }

    @Test
    void redisson() {
        RMap<MyKey, MyValue> map = redisson.getMap("myMap");
        RLock lock = redisson.getLock("myLock");
        RExecutorService executor = redisson.getExecutorService("myExecutorService");

        assertNotNull(map);
        assertNotNull(lock);
        assertNotNull(executor);
    }

    @Test
    void redissonReactive() {
        // Reactive API
        RedissonReactiveClient redissonReactive = redisson.reactive();
        RMapReactive<MyKey, MyValue> mapReactive = redissonReactive.getMap("myMap");
        RLockReactive lockReactive = redissonReactive.getLock("myLock");

        assertNotNull(mapReactive);
        assertNotNull(lockReactive);
    }

    @Test
    void redissonRx() {
        // RxJava3 API
        RedissonRxClient redissonRx = redisson.rxJava();
        RMapRx<MyKey, MyValue> mapRx = redissonRx.getMap("myMap");
        RLockRx lockRx = redissonRx.getLock("myLock");

        assertNotNull(mapRx);
        assertNotNull(lockRx);
    }

    @Test
    void testLock() throws InterruptedException {
        RLock lock = redisson.getLock("myLock");

        boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
        if (isLock) {
            try {
                log.info("do something");
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * ����һ����˼·
     *
     * ��      ��redis  ��mysql  дredis
        if(��redis������){
            return
        }
        if(��redis������){
            ����
                if(��redis������){
                    return
                }
                if(��redis������){
                    ��mysql
                    дredis
                }
            ����
        }

     * д      ɾredis  дmysql  ɾredis
        д��
            ɾredis
            дmysql
        ����
     */

}

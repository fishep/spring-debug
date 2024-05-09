package com.fishep.spring.debug.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class LuaScriptTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void compareAndSet() {
        DefaultRedisScript<Boolean> casScript = new DefaultRedisScript<>();
        casScript.setResultType(Boolean.class);
        casScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/compareAndSet.lua")));

        List<String> keys = new ArrayList<>();
        keys.add("compareAndSet");

        assertEquals(Boolean.TRUE, stringRedisTemplate.execute(casScript, keys, "0", "1"));
        assertEquals(Boolean.FALSE, stringRedisTemplate.execute(casScript, keys, "0", "1"));

        stringRedisTemplate.delete("compareAndSet");
    }

    @Test
    public void executeLuaScriptFromString() {
//        EVAL "local a = tonumber(ARGV[1])\nlocal b = tonumber(ARGV[2])\nreturn a + b" 1 kk 10 20
//        EVAL "local a = tonumber(ARGV[1])\nlocal b = tonumber(ARGV[2])\nreturn a + b" 0 10 20
        String luaScript = "local a = tonumber(ARGV[1])\nlocal b = tonumber(ARGV[2])\nreturn a + b";

        RedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        Object[] args = new Object[]{"10", "20"}; // 传递给Lua脚本的参数
        Long result = stringRedisTemplate.execute(script, new ArrayList<>(), args);

        assertEquals(10 + 20, result);
    }

    @Test
    public void executeLuaScriptFromFile() {
        Resource resource = resourceLoader.getResource("classpath:/lua/myscript.lua");
        String luaScript;
        try {
            luaScript = new String(resource.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Unable to read Lua script file.");
        }

        RedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        String[] keys = new String[0]; // 通常情况下，没有KEYS部分
        Object[] args = new Object[]{"10", "20"}; // 传递给Lua脚本的参数
        Long result = stringRedisTemplate.execute(script, List.of(keys), args);

        assertEquals(10 + 20, result);
    }

}

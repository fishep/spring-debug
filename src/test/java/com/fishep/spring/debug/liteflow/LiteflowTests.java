package com.fishep.spring.debug.liteflow;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author fly.fei
 * @Date 2024/8/16 16:30
 * @Desc
 **/
@Slf4j
@SpringBootTest
public class LiteflowTests {

    @Autowired
    private YourClass yourClass;

    @Test
    void flow() {
        yourClass.testConfig();
    }

}

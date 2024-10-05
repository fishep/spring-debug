package com.fishep.java;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @Author fly.fei
 * @Date 2024/9/25 10:47
 * @Desc
 **/
@Slf4j
public class Idtests {

    @Test
    void uuid() {
        UUID uuid = UUID.randomUUID();

        log.info("uuid: " + uuid.toString());
        log.info("uuid: " + uuid);
    }
}

package com.fishep.java;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Author fly.fei
 * @Date 2024/7/8 14:47
 * @Desc
 **/
@Slf4j
public class TimeTests {

    @Test
    void timezone() {
//        1720424734139
        Long timestamp = System.currentTimeMillis();
        log.info("currentTimeMillis:  " + timestamp);

//        1720424734144
        Long epochMilli = Instant.now().toEpochMilli();
        log.info("epochMilli:  " + epochMilli);

        Instant instant = Instant.now();
//        Instant instant = Instant.ofEpochMilli(timestamp);
        log.info("instant:  " + instant);

//        ZoneId zoneId = ZoneId.of("UTC");
        ZoneId zoneId = ZoneId.of("UTC+8");
        log.info("zoneId:  " + zoneId);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        log.info("localDateTime:  " + localDateTime);
        log.info("DateTimeFormatter:  " + DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss Z").withZone(zoneId).format(instant));

    }

    @Test
    void epoch() {
        Long epochSecond = Instant.now().getEpochSecond();
        log.info("epochSecond:  " + epochSecond); //1720425089

        Long epochMilli = Instant.now().toEpochMilli();
        log.info("epochMilli:  " + epochMilli);   //1720425089733
    }

}

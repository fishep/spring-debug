package com.fishep.spring.debug.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author fly.fei
 * @Date 2024/5/14 11:03
 * @Desc
 **/
@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KafkaTemplateTests {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static String topic = "topic-demo";

    private static String message = "this is a message from KafkaTemplate";

    @Test
    @Order(1)
    void send() throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

        SendResult<String, Object> result = future.get();

        assertNotNull(result);

        log.info(String.valueOf(result));
    }

}

package com.fishep.spring.debug.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/5/14 16:20
 * @Desc
 **/
@Slf4j
@Component
public class DemoConsumer {

    @KafkaListener(topics = {"topic-demo"})
    public void onMessage(ConsumerRecord<String, Object> record) {
        log.trace("topic: " + record.topic() + ", partition: " + record.partition() + ", key: " + record.key() + ", value: " + record.value());
    }

}

package com.chunlei.bili.danmaku.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
public class KafkaProducer {
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public void sendMessage(String topic, String msg, long timestamp,String key){
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, null, timestamp, key, msg);
        future.addCallback(result -> log.info("生产者成功发送消息到topic:{} partition:{}的消息", result.getRecordMetadata().topic(), result.getRecordMetadata().partition()),
                ex -> log.error("生产者发送消失败，原因：{}", ex.getMessage()));
    }
}

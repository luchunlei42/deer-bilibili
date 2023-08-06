package com.chunlei.bili.danmaku.component;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.danmaku.config.KafkaConfig;
import com.chunlei.bili.danmaku.service.WebSocketService;
import com.chunlei.bili.thumbup.model.Like;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KafkaConsumer {
    @Autowired
    StringRedisTemplate redisTemplate;

    @KafkaListener(topics = {KafkaConfig.SEND_TOPIC})
    public void consumeTimelineMessageBatch(List<ConsumerRecord<String,String>> consumerRecords, Acknowledgment ack) {
        Map<Long, Long> likeCountMap = new HashMap<>();
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try {
                String value = consumerRecord.value();
                JSONObject jsonObject = JSONObject.parseObject(value);
                String sessionId = jsonObject.getString("sessionId");
                String message = jsonObject.getString("message");
                // 根据sessionId获取对应的webSocketService
                WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(sessionId);

                // 判断会话是否还处于打开状态
                if (webSocketService.getSession().isOpen()) {
                    webSocketService.sendMessage(message);
                }
                ack.acknowledge();
            } catch(Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}

package com.chunlei.bili.danmaku.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.danmaku.config.KafkaConfig;
import com.chunlei.bili.danmaku.mapper.DanmakuMapper;
import com.chunlei.bili.danmaku.model.Danmaku;
import com.chunlei.bili.danmaku.service.DanmakuService;
import com.chunlei.bili.danmaku.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KafkaConsumerForLaunch {
    @Autowired
    DanmakuMapper danmakuMapper;

    @KafkaListener(topics = {KafkaConfig.DANMAKU_TOPIC})
    public void consumeTimelineMessageBatch(List<ConsumerRecord<String,String>> consumerRecords, Acknowledgment ack) {
        List<Danmaku> list = new ArrayList<>();
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try {
                String value = consumerRecord.value();
                Danmaku danmaku = JSON.parseObject(value, Danmaku.class);
                danmakuMapper.insertSelective(danmaku);
                ack.acknowledge();
            } catch(Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}

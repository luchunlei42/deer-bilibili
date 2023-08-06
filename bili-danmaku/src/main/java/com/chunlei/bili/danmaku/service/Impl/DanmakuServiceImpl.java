package com.chunlei.bili.danmaku.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.danmaku.component.KafkaProducer;
import com.chunlei.bili.danmaku.config.KafkaConfig;
import com.chunlei.bili.danmaku.model.Danmaku;
import com.chunlei.bili.danmaku.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DanmakuServiceImpl implements DanmakuService {

    private static final String DANMAKU_PREFIX = "danmaku:";
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public void addDanmakuToRedis(Danmaku danmaku) {
        String key = DANMAKU_PREFIX + danmaku.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmaku> list = new ArrayList<>();
        if (!StrUtil.isEmpty(value)){
            list = JSONArray.parseArray(value, Danmaku.class);
        }
        list.add(danmaku);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
    }

    @Override
    public void addDanmaku(Danmaku danmaku) {
        kafkaProducer.sendMessage(KafkaConfig.DANMAKU_TOPIC, JSON.toJSONString(danmaku),System.currentTimeMillis(),null);
    }
}

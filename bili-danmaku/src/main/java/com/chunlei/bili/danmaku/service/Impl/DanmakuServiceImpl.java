package com.chunlei.bili.danmaku.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.danmaku.component.KafkaProducer;
import com.chunlei.bili.danmaku.config.KafkaConfig;
import com.chunlei.bili.danmaku.mapper.DanmakuMapper;
import com.chunlei.bili.danmaku.model.Danmaku;
import com.chunlei.bili.danmaku.model.DanmakuExample;
import com.chunlei.bili.danmaku.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DanmakuServiceImpl implements DanmakuService {

    private static final String DANMAKU_PREFIX = "danmaku:";
    private static final Integer DANMAKU_EXPIRE = 3600;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    DanmakuMapper danmakuMapper;

    @Override
    public void addDanmakuToRedis(Danmaku danmaku) {
        String key = DANMAKU_PREFIX + danmaku.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmaku> list = new ArrayList<>();
        if (!StrUtil.isEmpty(value)){
            list = JSONArray.parseArray(value, Danmaku.class);
        }
        list.add(danmaku);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list),DANMAKU_EXPIRE);
    }

    @Override
    public void addDanmaku(Danmaku danmaku) {
        kafkaProducer.sendMessage(KafkaConfig.DANMAKU_TOPIC, JSON.toJSONString(danmaku),System.currentTimeMillis(),null);
    }

    @Override
    public Long getDanmakuCount(Long videoId) {
        String key = DANMAKU_PREFIX + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmaku> list = new ArrayList<>();
        if (StrUtil.isNotEmpty(value)){
            list = JSONArray.parseArray(value, Danmaku.class);
        } else {
            DanmakuExample example = new DanmakuExample();
            example.createCriteria().andVideoIdEqualTo(videoId);
            list = danmakuMapper.selectByExample(example);
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list),DANMAKU_EXPIRE);
        }
        return (long) list.size();
    }

    @Override
    public List<Object[]> getDanmaku(Long vid) {
        DanmakuExample danmakuExample = new DanmakuExample();
        danmakuExample.createCriteria().andVideoIdEqualTo(vid);
        String key = DANMAKU_PREFIX + vid;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmaku> danmakus;
        if (!StrUtil.isEmpty(value)){
            danmakus = JSONArray.parseArray(value, Danmaku.class);
        }else {
            danmakus = danmakuMapper.selectByExample(danmakuExample);
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(danmakus),DANMAKU_EXPIRE);
        }
        List<Object[]> res = new ArrayList<>();
        if (danmakus != null && !danmakus.isEmpty()){
            res = danmakus.stream().map(danmaku -> {
                Object[] list= new Object[]{new BigDecimal(danmaku.getDanmakuTime()),"right",danmaku.getColor(),danmaku.getAuthor(),danmaku.getContent()};
                return list;
            }).collect(Collectors.toList());
        }
        return res;
    }
}

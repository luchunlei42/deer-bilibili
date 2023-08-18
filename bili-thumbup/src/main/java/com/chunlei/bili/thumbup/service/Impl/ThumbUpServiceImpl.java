package com.chunlei.bili.thumbup.service.Impl;

import com.alibaba.fastjson.JSON;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.thumbup.component.KafkaProducer;
import com.chunlei.bili.thumbup.config.KafkaConfig;
import com.chunlei.bili.thumbup.config.RedisConfig;
import com.chunlei.bili.thumbup.model.Like;
import com.chunlei.bili.thumbup.service.ThumbUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ThumbUpServiceImpl implements ThumbUpService {
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void like(Like like) {

        Long memberId = UserHolder.getUser().getId();
        String redisKey = RedisConfig.LIKE_PREFIX+memberId;
        if (redisTemplate.opsForZSet().score(redisKey,like.getVideoId().toString()) != null){
            like.setStatus(false);
        } else {
            like.setStatus(true);
        }
        like.setLikedMemberId(memberId);
        like.setCreateTime(new Date());
        like.setUpdateTime(new Date());
        String jsonString = JSON.toJSONString(like);
        //异步处理
        kafkaProducer.sendMessage(KafkaConfig.LIKE_TOPIC,jsonString,System.currentTimeMillis(),like.getLikedMemberId().toString());
    }

    @Override
    public Long getLikeByVideoId(Long videoId) {

        String redisKey = RedisConfig.LIKE_COUNT_PREFIX+videoId;
        String s = redisTemplate.opsForValue().get(redisKey);
        if (s != null){
            return Long.valueOf(s);
        }
        return null;
    }

    @Override
    public Boolean isLike(Long videoId, Long memberId) {
        String redisKey = RedisConfig.LIKE_PREFIX+memberId;
        if (redisTemplate.opsForZSet().score(redisKey,videoId.toString()) != null){
            //查数据库
            return true;
        }
        return false;
    }
}

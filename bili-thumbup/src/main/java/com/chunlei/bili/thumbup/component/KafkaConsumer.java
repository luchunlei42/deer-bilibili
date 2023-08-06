package com.chunlei.bili.thumbup.component;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.chunlei.bili.moment.model.UserMoments;
import com.chunlei.bili.thumbup.config.KafkaConfig;
import com.chunlei.bili.thumbup.config.RedisConfig;
import com.chunlei.bili.thumbup.model.Like;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @KafkaListener(topics = {KafkaConfig.LIKE_TOPIC})
    public void consumeTimelineMessageBatch(List<ConsumerRecord<String,String>> consumerRecords, Acknowledgment ack){
        Map<Long,Long> likeCountMap = new HashMap<>();
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try{
                long timestamp = consumerRecord.timestamp();
                String value = consumerRecord.value();
                Like like = JSON.parseObject(value, Like.class);
                //TODO:检查视频是否存在
                Long memberId = like.getLikedMemberId();
                String likeKey = RedisConfig.LIKE_PREFIX+ memberId;
                Long videoId = like.getVideoId();
                if (like.getStatus()){
                    Long count = likeCountMap.getOrDefault(videoId, 0L)+1;
                    likeCountMap.put(videoId,count);
                    redisTemplate.opsForZSet().add(likeKey, videoId.toString(),timestamp);
                    redisTemplate.expire(likeKey,RedisConfig.LIKE_EXPIRE, TimeUnit.MINUTES);
                    //TODO: 截取2000，防止缓存过长

                    if (redisTemplate.opsForSet().isMember(RedisConfig.UPDATE_NOTLIKE_PREFIX+memberId%RedisConfig.shards,memberId+"-"+videoId)){
                        redisTemplate.opsForSet().remove(RedisConfig.UPDATE_NOTLIKE_PREFIX+memberId%RedisConfig.shards,memberId+"-"+videoId);
                    }
                    //将videoId-memberId放入set中，供异步持久化到mysql
                    redisTemplate.opsForSet().add(RedisConfig.UPDATE_LIKE_PREFIX+memberId%RedisConfig.shards,memberId+"-"+videoId);
                }else{
                    Long count = likeCountMap.getOrDefault(videoId, 0L)-1;
                    likeCountMap.put(videoId,count);

                    redisTemplate.opsForZSet().remove(likeKey, videoId);
                    if (redisTemplate.opsForSet().isMember(RedisConfig.UPDATE_LIKE_PREFIX+memberId%RedisConfig.shards,memberId+"-"+videoId)){
                        redisTemplate.opsForSet().remove(RedisConfig.UPDATE_LIKE_PREFIX+memberId%RedisConfig.shards,memberId+"-"+videoId);
                    }
                    //将videoId-memberId放入set中，供异步持久化到mysql
                    redisTemplate.opsForSet().add(RedisConfig.UPDATE_NOTLIKE_PREFIX+memberId%RedisConfig.shards,memberId+"-"+videoId);
                }


                ack.acknowledge();
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        likeCountMap.forEach((videoId,count)->{
            String likeCountKey = RedisConfig.LIKE_COUNT_PREFIX+videoId;
            redisTemplate.opsForValue().increment(likeCountKey,count);
            redisTemplate.expire(likeCountKey,RedisConfig.LIKE_COUNT_EXPIRE,TimeUnit.MINUTES);
            //将videoId放入set中，供异步持久化到mysql
            redisTemplate.opsForSet().add(RedisConfig.UPDATE_LIKE_COUNT_PREFIX+videoId%RedisConfig.shards,videoId.toString());
        });
    }
}

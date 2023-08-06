package com.chunlei.bili.reply.component;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson.JSON;
import com.chunlei.bili.reply.client.VideoFeignClient;
import com.chunlei.bili.reply.config.KafkaConfig;
import com.chunlei.bili.reply.config.RedisConfig;
import com.chunlei.bili.reply.dao.ReplyDao;
import com.chunlei.bili.reply.dto.ReplyResult;
import com.chunlei.bili.reply.mapper.ReplyMapper;
import com.chunlei.bili.reply.model.Reply;
import com.chunlei.bili.thumbup.model.Like;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KafkaConsumer {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ReplyDao replyDao;
    @Autowired
    VideoFeignClient videoFeignClient;

    @KafkaListener(topics = {KafkaConfig.REPLY_TOPIC})
    public void consumeReplyMessageBatch(List<ConsumerRecord<String,String>> consumerRecords, Acknowledgment ack){
        Map<Long, Long> replyIncrementCountMap = new HashMap<>();
        Map<Long, List<Pair<String,Double>>> videoToReplyMap = new HashMap<>();
        List<Reply> replyList = new ArrayList<>();
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try{
                String value = consumerRecord.value();
                Reply reply = JSON.parseObject(value, Reply.class);
                replyList.add(reply);
                Long videoId = reply.getVideoId();
                Long orDefault = replyIncrementCountMap.getOrDefault(videoId, 0L);
                replyIncrementCountMap.put(videoId,orDefault+1);
                videoToReplyMap.computeIfAbsent(videoId,v->new ArrayList<>()).add(Pair.of(reply.getId().toString(), (double) reply.getCreateTime().getTime()));
                //TODO:更新用户的数据，子评论数，根评论数
                ack.acknowledge();
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        //评论,评论数插入db
        replyDao.insertBatch(replyList);
        videoFeignClient.incrementReplyCount(replyIncrementCountMap);
        videoToReplyMap.forEach((k,v)->{
            Long videoId = k;
            List<Pair<String,Double>> replies = v;
            String replyIndexKey = RedisConfig.REPLY_INDEX_PREFIX+videoId;
            Set<ZSetOperations.TypedTuple<String>> typedTuples = replies.stream().map(item -> {
                ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<>(item.getKey(), item.getValue());
                return tuple;
            }).collect(Collectors.toSet());
            if (redisTemplate.expire(replyIndexKey, RedisConfig.REPLY_INDEX_EXPIRE,TimeUnit.HOURS)){
                redisTemplate.opsForZSet().add(replyIndexKey,typedTuples);
            }else{
                //key不存在，不进行增量更新，等待缓存重建
            }
        });
    }
}

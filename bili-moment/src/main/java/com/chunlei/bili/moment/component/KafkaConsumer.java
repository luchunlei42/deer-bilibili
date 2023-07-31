package com.chunlei.bili.moment.component;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.moment.client.MemberFeignClient;
import com.chunlei.bili.moment.config.KafkaConfig;
import com.chunlei.bili.moment.config.RedisConfig;
import com.chunlei.bili.moment.dto.FollowingDTO;
import com.chunlei.bili.moment.model.UserMoments;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumer {
    @Autowired
    MemberFeignClient memberFeignClient;
    @Value("${push-pull-point}")
    Long PushPullDiffPoint;
    @Autowired
    StringRedisTemplate redisTemplate;

    @KafkaListener(topics = {KafkaConfig.TIMELINE_TOPIC})
    public void consumeTimelineMessageBatch(List<ConsumerRecord<String,String>> consumerRecords, Acknowledgment ack){
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try{
                long timestamp = consumerRecord.timestamp();
                String value = consumerRecord.value();
                UserMoments moments = JSON.parseObject(value, UserMoments.class);

                Long memberId = moments.getMemberId();
                //确定用户粉丝数，如果粉丝数少，使用push模式，粉丝数高，使用pull模式

                R<List<FollowingDTO>> r = memberFeignClient.getUserFansAll(memberId);
                if (r.getCode() != 200){
                    log.error("获取用户粉丝错误");
                    ack.acknowledge();
                    return;
                }
                List<FollowingDTO> fans = r.getData();

                if (BeanUtil.isEmpty(fans)){
                    ack.acknowledge();
                    return;
                }

                if (fans.size() > PushPullDiffPoint){
                    //使用pull模式，用户自行pull动态
                    ack.acknowledge();
                    return;
                }else{
                    //使用push模式，push动态到用户收件箱
                    for (FollowingDTO fan : fans) {
                        Long fanId = fan.getId();
                        String redisKey = RedisConfig.TIMELINE_PREFIX+fanId;
                        redisTemplate.opsForZSet().add(redisKey,value,timestamp);
                    }
                }
                ack.acknowledge();
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}

package com.chunlei.bili.moment.service.Impl;

import com.alibaba.fastjson.JSON;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.IdGeneratorSnowflake;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.moment.client.MemberFeignClient;
import com.chunlei.bili.moment.component.KafkaProducer;
import com.chunlei.bili.moment.config.KafkaConfig;
import com.chunlei.bili.moment.config.RedisConfig;
import com.chunlei.bili.moment.dto.ScrollResult;
import com.chunlei.bili.moment.mapper.UserMomentsMapper;
import com.chunlei.bili.moment.model.UserMoments;
import com.chunlei.bili.moment.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.chunlei.bili.moment.config.RedisConfig.MOMENT_PREFIX;

@Service
public class MomentServiceImpl implements MomentService {
    @Autowired
    IdGeneratorSnowflake idGenerator;
    @Autowired
    UserMomentsMapper momentsMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    MemberFeignClient memberFeignClient;

    @Override
    public void addMoment(UserMoments moments) {
        Long memberId = UserHolder.getUser().getId();
        moments.setMemberId(memberId);
        //生成唯一contentId
        long contentId = idGenerator.snowflakeId();
        moments.setContentId(contentId);

        //TODO: 异步存储动态内容

        moments.setCreatetime(new Date());
        moments.setUpdatetime(new Date());

        //动态保存到db
        momentsMapper.insertSelective(moments);
        //动态保存到redis
        long l = System.currentTimeMillis();
        String redisKey = MOMENT_PREFIX+memberId;
        String jsonString = JSON.toJSONString(moments);
        redisTemplate.opsForZSet().add(redisKey,jsonString,l);

        //发送kafka消息处理动态推送
        kafkaProducer.sendMessage(KafkaConfig.TIMELINE_TOPIC,jsonString,l);
    }



    @Override
    public R queryMomentOfFollow(Long max, Long offset) {
        Long memberId = UserHolder.getUser().getId();

        //首先查看有没有关注大UP主，从大UP直接pull动态
        pullMomentsFromFamous(getFamous(memberId));

        //查询收件箱
        String redisKey = RedisConfig.TIMELINE_PREFIX+memberId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(redisKey, 0, max, offset, 5);
        if (typedTuples==null || typedTuples.isEmpty()){
            return R.success(null);
        }

        List<Long> contentIds = new ArrayList<>(typedTuples.size());
        Map<Long, UserMoments> map  =new HashMap<>();
        long minTime = 0;
        int os = 1;

        for (ZSetOperations.TypedTuple<String> tuple : typedTuples){
            String json = tuple.getValue();
            UserMoments moments = JSON.parseObject(json, UserMoments.class);
            Long contentId = moments.getContentId();
            long time = tuple.getScore().longValue();
            contentIds.add(contentId);
            map.put(contentId,moments);
            if (time == minTime){
                os++;
            }else{
                minTime = time;
                os = 1;
            }
        }

        //TODO：查询动态内容

        ScrollResult r = new ScrollResult();
        r.setList(map.values().stream().collect(Collectors.toList()));
        r.setOffset(os);
        r.setMinTime(minTime);
        return R.success(r);

    }

    @Override
    public List<Long> getFamous(Long memberId) {
        String redisKey = RedisConfig.FAMOUS_PREFIX+memberId;
        String s = redisTemplate.opsForValue().get(redisKey);
        return JSON.parseObject(s,List.class);
    }

    @Override
    public void pullMomentsFromFamous(List<Long> ids) {
        for (Long id : ids) {
            String redisKey = MOMENT_PREFIX+id;
            redisTemplate.opsForZSet().reverseRangeByScoreWithScores(redisKey,0,System.currentTimeMillis(),0,200);
        }
    }

}

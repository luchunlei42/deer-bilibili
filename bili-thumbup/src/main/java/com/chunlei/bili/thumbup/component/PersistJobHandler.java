package com.chunlei.bili.thumbup.component;

import com.chunlei.bili.thumbup.config.RedisConfig;
import com.chunlei.bili.thumbup.dao.LikeCountDao;
import com.chunlei.bili.thumbup.dao.LikeDao;
import com.chunlei.bili.thumbup.mapper.LikeCountMapper;
import com.chunlei.bili.thumbup.mapper.LikeMapper;
import com.chunlei.bili.thumbup.model.Like;
import com.chunlei.bili.thumbup.model.LikeCount;
import com.chunlei.bili.thumbup.model.LikeExample;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.chunlei.bili.thumbup.config.RedisConfig.*;

@Component
@Slf4j
public class PersistJobHandler {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    LikeDao likeDao;
    @Autowired
    LikeCountDao likeCountDao;
    @XxlJob("persistJob")
    public ReturnT<String> persistJob(){
        //未来将分片进行持久化

        for (int i=0;i< shards;i++){
            String updateLikeCountKey = UPDATE_LIKE_COUNT_PREFIX+i;
            String updateLikeKey = UPDATE_LIKE_PREFIX+i;
            String updateNotLikeKey = UPDATE_NOTLIKE_PREFIX+i;
            Set<String> videos = redisTemplate.opsForSet().members(updateLikeCountKey);
            Set<String> members = redisTemplate.opsForSet().members(updateLikeKey);
            Set<String> delMembers = redisTemplate.opsForSet().members(updateNotLikeKey);
            List<Like> likes = new ArrayList<>();
            List<Like> delLikes = new ArrayList<>();
            List<LikeCount> likeCounts = new ArrayList<>();
            for (String val : members) {
                String[] str = val.split("-");
                String memberId = str[0];
                String videoId = str[1];
                String likeKey = LIKE_PREFIX+memberId;
                long timestamp = Math.round(redisTemplate.opsForZSet().score(likeKey, videoId));
                Like like = new Like();
                like.setLikedMemberId(Long.valueOf(memberId));
                like.setUpdateTime(new Date(timestamp));
                like.setVideoId(Long.valueOf(videoId));
                like.setStatus(true);
                likes.add(like);
            }
            for (String videoId : videos) {
                String likeCountKey = LIKE_COUNT_PREFIX+videoId;
                String s = redisTemplate.opsForValue().get(likeCountKey);
                long count = Long.parseLong(s);
                LikeCount likeCount = new LikeCount();
                likeCount.setCount(count);
                likeCount.setUpdateTime(new Date());
                likeCount.setVideoId(Long.valueOf(videoId));
                likeCounts.add(likeCount);
            }
            for (String delMember : delMembers) {
                String[] str = delMember.split("-");
                String memberId = str[0];
                String videoId = str[1];
                Like delLike = new Like();
                delLike.setVideoId(Long.valueOf(videoId));
                delLike.setLikedMemberId(Long.valueOf(memberId));
                delLike.setStatus(false);
                delLike.setUpdateTime(new Date());
                delLikes.add(delLike);
            }
            likeDao.insertOrUpdateBatch(likes);
            likeDao.deleteBatch(delLikes);
            likeCountDao.insertOrUpdateBatch(likeCounts);

        }
        return ReturnT.SUCCESS;
    }
}

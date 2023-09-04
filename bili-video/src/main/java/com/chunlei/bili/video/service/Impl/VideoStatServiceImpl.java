package com.chunlei.bili.video.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.chunlei.bili.common.utils.RedisConstants;
import com.chunlei.bili.video.client.BiliThumbUpFeignClient;
import com.chunlei.bili.video.dao.VideoStatDao;
import com.chunlei.bili.video.mapper.VideoStatMapper;
import com.chunlei.bili.video.model.VideoStat;
import com.chunlei.bili.video.model.VideoStatExample;
import com.chunlei.bili.video.service.VideoStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class VideoStatServiceImpl implements VideoStatService {
    @Autowired
    VideoStatDao videoStatDao;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    VideoStatMapper videoStatMapper;
    @Autowired
    BiliThumbUpFeignClient thumbUpFeignClient;

    @Override
    public void incrementReplyCount(Map<Long, Long> map) {
        videoStatDao.incrementReplyCountBatch(map);
    }

    @Override
    public Long getVideoView(Long videoId) {

        String key = RedisConstants.VIDEO_VIEW+videoId;
        return Optional.of(redisTemplate.opsForHyperLogLog().size(key)).orElse(0L);
    }

    @Override
    public void setVideoView(Long videoId, Long memberId) {
        String key = RedisConstants.VIDEO_VIEW+videoId;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String date = sdf1.format(new Date());
        redisTemplate.opsForHyperLogLog().add(memberId+date);
    }

    @Override
    public Long getReplyCount(Long videoId) {
        String key = RedisConstants.REPLY_COUNT+videoId;
        String s = redisTemplate.opsForValue().get(key);
        Long res = 0L;
        if (StrUtil.isEmpty(s)){
            VideoStatExample example = new VideoStatExample();
            example.createCriteria().andVideoIdEqualTo(videoId);
            List<VideoStat> videoStatList = videoStatMapper.selectByExample(example);
            if (videoStatList != null && videoStatList.size()>0){
                VideoStat videoStat = videoStatList.get(0);
                System.out.println("videoReply:"+videoStat.getReply().toString());
                redisTemplate.opsForValue().set(key,videoStat.getReply().toString(),RedisConstants.REPLY_COUNT_TTL*3600, TimeUnit.SECONDS);
                res = videoStat.getReply();
            } else {
                //防止缓存穿透
                redisTemplate.opsForValue().set(key,"", RedisConstants.CACHE_NULL_TTL);
            }
        }else {
            res = Long.parseLong(s);
        }
        return res;
    }

    @Override
    public VideoStat getVideoStat(Long videoId) {

        VideoStat videoStat = new VideoStat();
        videoStat.setReply(getReplyCount(videoId));
        videoStat.setView(getVideoView(videoId));
        videoStat.setVideoId(videoId);
        videoStat.setLike(thumbUpFeignClient.getLikeByVideoId(videoId).getData());
        return videoStat;
    }
}

package com.chunlei.bili.video.service.Impl;

import com.chunlei.bili.video.dao.VideoStatDao;
import com.chunlei.bili.video.service.VideoStatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class VideoStatServiceImpl implements VideoStatService {
    @Autowired
    VideoStatDao videoStatDao;

    @Override
    public void incrementReplyCount(Map<Long, Long> map) {
        videoStatDao.incrementReplyCountBatch(map);
    }
}

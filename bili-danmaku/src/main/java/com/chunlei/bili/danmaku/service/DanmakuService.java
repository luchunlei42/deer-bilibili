package com.chunlei.bili.danmaku.service;

import com.chunlei.bili.danmaku.model.Danmaku;

import java.util.List;

public interface DanmakuService {
    void addDanmakuToRedis(Danmaku danmaku);

    void addDanmaku(Danmaku danmaku);

    Long getDanmakuCount(Long videoId);

    List<Object[]> getDanmaku(Long vid);
}

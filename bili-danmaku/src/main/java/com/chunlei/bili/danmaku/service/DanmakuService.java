package com.chunlei.bili.danmaku.service;

import com.chunlei.bili.danmaku.model.Danmaku;

public interface DanmakuService {
    void addDanmakuToRedis(Danmaku danmaku);

    void addDanmaku(Danmaku danmaku);
}

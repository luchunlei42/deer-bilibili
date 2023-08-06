package com.chunlei.bili.video.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface VideoStatDao {
    void incrementReplyCountBatch(@Param("map") Map<Long, Long> map);
}

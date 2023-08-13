package com.chunlei.bili.video.service;

import com.chunlei.bili.video.model.VideoStat;

import java.util.Map;

public interface VideoStatService {
    void incrementReplyCount(Map<Long, Long> map);

    Long getVideoView(Long videoId);

    void setVideoView(Long videoId, Long memberId);

    Long getReplyCount(Long videoId);

    VideoStat getVideoStat(Long videoId);
}

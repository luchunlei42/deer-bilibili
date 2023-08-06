package com.chunlei.bili.thumbup.service;

import com.chunlei.bili.thumbup.model.Like;

public interface ThumbUpService {
    void like(Like like);

    Long getLikeByVideoId(Long videoId);
}

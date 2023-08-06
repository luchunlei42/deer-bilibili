package com.chunlei.bili.thumbup.dao;

import com.chunlei.bili.thumbup.model.LikeCount;

import java.util.List;

public interface LikeCountDao {


    void insertOrUpdateBatch(List<LikeCount> likeCounts);
}

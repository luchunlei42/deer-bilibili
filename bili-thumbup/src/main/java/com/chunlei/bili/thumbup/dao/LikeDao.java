package com.chunlei.bili.thumbup.dao;

import com.chunlei.bili.thumbup.model.Like;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LikeDao {

    void insertOrUpdateBatch(@Param("likes") List<Like> likes);

    void deleteBatch(List<Like> delLikes);
}

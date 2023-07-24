package com.chunlei.bili.video.dao;

import com.chunlei.bili.video.model.VideoTagRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VideoTagRelationDao {
    void saveBatch(List<VideoTagRelation> tagRelationList);
}

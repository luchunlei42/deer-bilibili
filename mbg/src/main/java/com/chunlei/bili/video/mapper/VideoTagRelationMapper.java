package com.chunlei.bili.video.mapper;

import com.chunlei.bili.video.model.VideoTagRelation;
import com.chunlei.bili.video.model.VideoTagRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VideoTagRelationMapper {
    long countByExample(VideoTagRelationExample example);

    int deleteByExample(VideoTagRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VideoTagRelation row);

    int insertSelective(VideoTagRelation row);

    List<VideoTagRelation> selectByExample(VideoTagRelationExample example);

    VideoTagRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") VideoTagRelation row, @Param("example") VideoTagRelationExample example);

    int updateByExample(@Param("row") VideoTagRelation row, @Param("example") VideoTagRelationExample example);

    int updateByPrimaryKeySelective(VideoTagRelation row);

    int updateByPrimaryKey(VideoTagRelation row);
}
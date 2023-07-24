package com.chunlei.bili.video.mapper;

import com.chunlei.bili.video.model.VideoDetail;
import com.chunlei.bili.video.model.VideoDetailExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoDetailMapper {
    long countByExample(VideoDetailExample example);

    int deleteByExample(VideoDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VideoDetail row);

    int insertSelective(VideoDetail row);

    List<VideoDetail> selectByExample(VideoDetailExample example);

    VideoDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") VideoDetail row, @Param("example") VideoDetailExample example);

    int updateByExample(@Param("row") VideoDetail row, @Param("example") VideoDetailExample example);

    int updateByPrimaryKeySelective(VideoDetail row);

    int updateByPrimaryKey(VideoDetail row);
}
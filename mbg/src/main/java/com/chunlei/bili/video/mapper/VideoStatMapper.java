package com.chunlei.bili.video.mapper;

import com.chunlei.bili.video.model.VideoStat;
import com.chunlei.bili.video.model.VideoStatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VideoStatMapper {
    long countByExample(VideoStatExample example);

    int deleteByExample(VideoStatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VideoStat row);

    int insertSelective(VideoStat row);

    List<VideoStat> selectByExample(VideoStatExample example);

    VideoStat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") VideoStat row, @Param("example") VideoStatExample example);

    int updateByExample(@Param("row") VideoStat row, @Param("example") VideoStatExample example);

    int updateByPrimaryKeySelective(VideoStat row);

    int updateByPrimaryKey(VideoStat row);
}
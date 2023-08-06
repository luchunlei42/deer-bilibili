package com.chunlei.bili.thumbup.mapper;

import com.chunlei.bili.thumbup.model.LikeCount;
import com.chunlei.bili.thumbup.model.LikeCountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LikeCountMapper {
    long countByExample(LikeCountExample example);

    int deleteByExample(LikeCountExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LikeCount row);

    int insertSelective(LikeCount row);

    List<LikeCount> selectByExample(LikeCountExample example);

    LikeCount selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") LikeCount row, @Param("example") LikeCountExample example);

    int updateByExample(@Param("row") LikeCount row, @Param("example") LikeCountExample example);

    int updateByPrimaryKeySelective(LikeCount row);

    int updateByPrimaryKey(LikeCount row);
}
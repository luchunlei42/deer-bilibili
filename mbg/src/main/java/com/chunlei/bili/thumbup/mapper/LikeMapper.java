package com.chunlei.bili.thumbup.mapper;

import com.chunlei.bili.thumbup.model.Like;
import com.chunlei.bili.thumbup.model.LikeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LikeMapper {
    long countByExample(LikeExample example);

    int deleteByExample(LikeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Like row);

    int insertSelective(Like row);

    List<Like> selectByExample(LikeExample example);

    Like selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Like row, @Param("example") LikeExample example);

    int updateByExample(@Param("row") Like row, @Param("example") LikeExample example);

    int updateByPrimaryKeySelective(Like row);

    int updateByPrimaryKey(Like row);
}
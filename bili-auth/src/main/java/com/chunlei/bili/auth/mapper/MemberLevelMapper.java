package com.chunlei.bili.auth.mapper;

import com.chunlei.bili.auth.model.MemberLevel;
import com.chunlei.bili.auth.model.MemberLevelExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberLevelMapper {
    long countByExample(MemberLevelExample example);

    int deleteByExample(MemberLevelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MemberLevel row);

    int insertSelective(MemberLevel row);

    List<MemberLevel> selectByExample(MemberLevelExample example);

    MemberLevel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") MemberLevel row, @Param("example") MemberLevelExample example);

    int updateByExample(@Param("row") MemberLevel row, @Param("example") MemberLevelExample example);

    int updateByPrimaryKeySelective(MemberLevel row);

    int updateByPrimaryKey(MemberLevel row);
}
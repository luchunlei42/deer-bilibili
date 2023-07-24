package com.chunlei.bili.auth.mapper;

import com.chunlei.bili.auth.model.MemberStatisticsInfo;
import com.chunlei.bili.auth.model.MemberStatisticsInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberStatisticsInfoMapper {
    long countByExample(MemberStatisticsInfoExample example);

    int deleteByExample(MemberStatisticsInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MemberStatisticsInfo row);

    int insertSelective(MemberStatisticsInfo row);

    List<MemberStatisticsInfo> selectByExample(MemberStatisticsInfoExample example);

    MemberStatisticsInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") MemberStatisticsInfo row, @Param("example") MemberStatisticsInfoExample example);

    int updateByExample(@Param("row") MemberStatisticsInfo row, @Param("example") MemberStatisticsInfoExample example);

    int updateByPrimaryKeySelective(MemberStatisticsInfo row);

    int updateByPrimaryKey(MemberStatisticsInfo row);
}
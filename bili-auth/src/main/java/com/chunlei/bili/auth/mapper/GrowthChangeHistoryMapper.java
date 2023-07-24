package com.chunlei.bili.auth.mapper;

import com.chunlei.bili.auth.model.GrowthChangeHistory;
import com.chunlei.bili.auth.model.GrowthChangeHistoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GrowthChangeHistoryMapper {
    long countByExample(GrowthChangeHistoryExample example);

    int deleteByExample(GrowthChangeHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GrowthChangeHistory row);

    int insertSelective(GrowthChangeHistory row);

    List<GrowthChangeHistory> selectByExample(GrowthChangeHistoryExample example);

    GrowthChangeHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") GrowthChangeHistory row, @Param("example") GrowthChangeHistoryExample example);

    int updateByExample(@Param("row") GrowthChangeHistory row, @Param("example") GrowthChangeHistoryExample example);

    int updateByPrimaryKeySelective(GrowthChangeHistory row);

    int updateByPrimaryKey(GrowthChangeHistory row);
}
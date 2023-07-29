package com.chunlei.bili.member.mapper;

import com.chunlei.bili.member.model.FollowingGroup;
import com.chunlei.bili.member.model.FollowingGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FollowingGroupMapper {
    long countByExample(FollowingGroupExample example);

    int deleteByExample(FollowingGroupExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FollowingGroup row);

    int insertSelective(FollowingGroup row);

    List<FollowingGroup> selectByExample(FollowingGroupExample example);

    FollowingGroup selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") FollowingGroup row, @Param("example") FollowingGroupExample example);

    int updateByExample(@Param("row") FollowingGroup row, @Param("example") FollowingGroupExample example);

    int updateByPrimaryKeySelective(FollowingGroup row);

    int updateByPrimaryKey(FollowingGroup row);
}
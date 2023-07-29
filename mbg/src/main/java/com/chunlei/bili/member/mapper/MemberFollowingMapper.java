package com.chunlei.bili.member.mapper;

import com.chunlei.bili.member.model.MemberFollowing;
import com.chunlei.bili.member.model.MemberFollowingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberFollowingMapper {
    long countByExample(MemberFollowingExample example);

    int deleteByExample(MemberFollowingExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MemberFollowing row);

    int insertSelective(MemberFollowing row);

    List<MemberFollowing> selectByExample(MemberFollowingExample example);

    MemberFollowing selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") MemberFollowing row, @Param("example") MemberFollowingExample example);

    int updateByExample(@Param("row") MemberFollowing row, @Param("example") MemberFollowingExample example);

    int updateByPrimaryKeySelective(MemberFollowing row);

    int updateByPrimaryKey(MemberFollowing row);
}
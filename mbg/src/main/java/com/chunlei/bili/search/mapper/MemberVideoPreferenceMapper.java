package com.chunlei.bili.search.mapper;

import com.chunlei.bili.search.model.MemberVideoPreference;
import com.chunlei.bili.search.model.MemberVideoPreferenceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberVideoPreferenceMapper {
    long countByExample(MemberVideoPreferenceExample example);

    int deleteByExample(MemberVideoPreferenceExample example);

    int deleteByPrimaryKey(@Param("memberId") Long memberId, @Param("videoId") Long videoId);

    int insert(MemberVideoPreference row);

    int insertSelective(MemberVideoPreference row);

    List<MemberVideoPreference> selectByExample(MemberVideoPreferenceExample example);

    MemberVideoPreference selectByPrimaryKey(@Param("memberId") Long memberId, @Param("videoId") Long videoId);

    int updateByExampleSelective(@Param("row") MemberVideoPreference row, @Param("example") MemberVideoPreferenceExample example);

    int updateByExample(@Param("row") MemberVideoPreference row, @Param("example") MemberVideoPreferenceExample example);

    int updateByPrimaryKeySelective(MemberVideoPreference row);

    int updateByPrimaryKey(MemberVideoPreference row);
}
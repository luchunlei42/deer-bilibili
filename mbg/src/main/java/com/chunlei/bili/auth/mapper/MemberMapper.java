package com.chunlei.bili.auth.mapper;

import com.chunlei.bili.auth.model.Member;
import com.chunlei.bili.auth.model.MemberExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
    long countByExample(MemberExample example);

    int deleteByExample(MemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Member row);

    int insertSelective(Member row);

    List<Member> selectByExample(MemberExample example);

    Member selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Member row, @Param("example") MemberExample example);

    int updateByExample(@Param("row") Member row, @Param("example") MemberExample example);

    int updateByPrimaryKeySelective(Member row);

    int updateByPrimaryKey(Member row);
}
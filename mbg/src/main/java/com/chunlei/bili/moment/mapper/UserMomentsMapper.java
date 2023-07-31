package com.chunlei.bili.moment.mapper;

import com.chunlei.bili.moment.model.UserMoments;
import com.chunlei.bili.moment.model.UserMomentsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMomentsMapper {
    long countByExample(UserMomentsExample example);

    int deleteByExample(UserMomentsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserMoments row);

    int insertSelective(UserMoments row);

    List<UserMoments> selectByExample(UserMomentsExample example);

    UserMoments selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UserMoments row, @Param("example") UserMomentsExample example);

    int updateByExample(@Param("row") UserMoments row, @Param("example") UserMomentsExample example);

    int updateByPrimaryKeySelective(UserMoments row);

    int updateByPrimaryKey(UserMoments row);
}
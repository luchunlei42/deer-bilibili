package com.chunlei.bili.video.mapper;

import com.chunlei.bili.video.model.CategoryTagRelation;
import com.chunlei.bili.video.model.CategoryTagRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryTagRelationMapper {
    long countByExample(CategoryTagRelationExample example);

    int deleteByExample(CategoryTagRelationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CategoryTagRelation row);

    int insertSelective(CategoryTagRelation row);

    List<CategoryTagRelation> selectByExample(CategoryTagRelationExample example);

    CategoryTagRelation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") CategoryTagRelation row, @Param("example") CategoryTagRelationExample example);

    int updateByExample(@Param("row") CategoryTagRelation row, @Param("example") CategoryTagRelationExample example);

    int updateByPrimaryKeySelective(CategoryTagRelation row);

    int updateByPrimaryKey(CategoryTagRelation row);
}
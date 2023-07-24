package com.chunlei.bili.video.mapper;

import com.chunlei.bili.video.model.Category;
import com.chunlei.bili.video.model.CategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryMapper {
    long countByExample(CategoryExample example);

    int deleteByExample(CategoryExample example);

    int deleteByPrimaryKey(Long catId);

    int insert(Category row);

    int insertSelective(Category row);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Long catId);

    int updateByExampleSelective(@Param("row") Category row, @Param("example") CategoryExample example);

    int updateByExample(@Param("row") Category row, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category row);

    int updateByPrimaryKey(Category row);
}
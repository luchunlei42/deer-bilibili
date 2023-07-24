package com.chunlei.bili.video.mapper;

import com.chunlei.bili.video.model.Tag;
import com.chunlei.bili.video.model.TagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TagMapper {
    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(Long tagId);

    int insert(Tag row);

    int insertSelective(Tag row);

    List<Tag> selectByExampleWithBLOBs(TagExample example);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Long tagId);

    int updateByExampleSelective(@Param("row") Tag row, @Param("example") TagExample example);

    int updateByExampleWithBLOBs(@Param("row") Tag row, @Param("example") TagExample example);

    int updateByExample(@Param("row") Tag row, @Param("example") TagExample example);

    int updateByPrimaryKeySelective(Tag row);

    int updateByPrimaryKeyWithBLOBs(Tag row);

    int updateByPrimaryKey(Tag row);
}
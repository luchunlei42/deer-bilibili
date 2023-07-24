package com.chunlei.bili.video.dao;

import com.chunlei.bili.video.model.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagDao {

    List<Tag> getTagsByCatId(Long catId);
}

package com.chunlei.bili.danmaku.mapper;

import com.chunlei.bili.danmaku.model.Danmaku;
import com.chunlei.bili.danmaku.model.DanmakuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DanmakuMapper {
    long countByExample(DanmakuExample example);

    int deleteByExample(DanmakuExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Danmaku row);

    int insertSelective(Danmaku row);

    List<Danmaku> selectByExampleWithBLOBs(DanmakuExample example);

    List<Danmaku> selectByExample(DanmakuExample example);

    Danmaku selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Danmaku row, @Param("example") DanmakuExample example);

    int updateByExampleWithBLOBs(@Param("row") Danmaku row, @Param("example") DanmakuExample example);

    int updateByExample(@Param("row") Danmaku row, @Param("example") DanmakuExample example);

    int updateByPrimaryKeySelective(Danmaku row);

    int updateByPrimaryKeyWithBLOBs(Danmaku row);

    int updateByPrimaryKey(Danmaku row);
}
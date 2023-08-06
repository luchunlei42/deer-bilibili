package com.chunlei.bili.reply.mapper;

import com.chunlei.bili.reply.model.Reply;
import com.chunlei.bili.reply.model.ReplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReplyMapper {
    long countByExample(ReplyExample example);

    int deleteByExample(ReplyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Reply row);

    int insertSelective(Reply row);

    List<Reply> selectByExample(ReplyExample example);

    Reply selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Reply row, @Param("example") ReplyExample example);

    int updateByExample(@Param("row") Reply row, @Param("example") ReplyExample example);

    int updateByPrimaryKeySelective(Reply row);

    int updateByPrimaryKey(Reply row);
}
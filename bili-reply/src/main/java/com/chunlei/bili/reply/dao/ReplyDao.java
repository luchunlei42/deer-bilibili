package com.chunlei.bili.reply.dao;

import com.chunlei.bili.reply.model.Reply;

import java.util.List;

public interface ReplyDao {

    void insertBatch(List<Reply> replyList);


    List<Reply> getRootRepliesWithTime(Long videoId);
}

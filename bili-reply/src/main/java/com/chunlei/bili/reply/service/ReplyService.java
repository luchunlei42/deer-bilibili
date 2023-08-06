package com.chunlei.bili.reply.service;

import com.chunlei.bili.reply.dto.ReplyResult;
import com.chunlei.bili.reply.model.Reply;

import java.util.concurrent.ExecutionException;

public interface ReplyService {
    void postReply(Reply reply);

    ReplyResult getReplyMain(Long videoId, Integer ps, Integer pn, Integer cursor) throws ExecutionException, InterruptedException;
}

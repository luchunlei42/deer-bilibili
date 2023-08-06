package com.chunlei.bili.reply.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.reply.dto.ReplyResult;
import com.chunlei.bili.reply.model.Reply;
import com.chunlei.bili.reply.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping("/post")
    public R postReply(@RequestBody Reply reply){
        if (reply == null || reply.getVideoId() == null){
            return R.failed("参数不能为空");
        }
        replyService.postReply(reply);
        return R.success(null);
    }

    /**
     * 分页获取根评论下的自评
     * @param videoId
     * @param rootId
     * @param ps
     * @param pn
     * @return
     */
    @GetMapping("/list")
    public R getReply(@RequestParam("vid") Long videoId,
                      @RequestParam("root") Long rootId,
                      @RequestParam(value = "ps", defaultValue = "5") Integer ps,
                      @RequestParam(value = "pn", defaultValue = "1") Integer pn){
        if (videoId == null || rootId == null){
            return R.success(null);
        }
        return null;
    }


    /**
     * 分页获取根评论
     * @param videoId
     * @param ps
     * @param pn
     * @return
     */
    @GetMapping("/main")
    public R getReplyMain(@RequestParam("vid") Long videoId,
                          @RequestParam(value = "ps", defaultValue = "5") Integer ps,
                          @RequestParam(value = "pn") Integer pn,
                          @RequestParam("cursor") Integer cursor) throws ExecutionException, InterruptedException {
        if (videoId == null){
            return R.success(null);
        }
        ReplyResult replyResult = replyService.getReplyMain(videoId,ps,pn,cursor);
        return R.success(replyResult);
    }


}

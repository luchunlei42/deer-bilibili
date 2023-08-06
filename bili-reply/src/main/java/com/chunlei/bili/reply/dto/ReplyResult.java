package com.chunlei.bili.reply.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReplyResult {
    private Integer num;
    private Integer size;
    private Integer next;
    private Boolean isEnd;
    List<ReplyDTO> replies;
}

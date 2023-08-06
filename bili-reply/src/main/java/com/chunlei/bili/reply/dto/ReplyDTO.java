package com.chunlei.bili.reply.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReplyDTO {

    private Long rpid;
    private Long vid;
    private MemberInfo memberInfo;
    private String content;
    private List<ReplyDTO> replies;
    private Date time;
}

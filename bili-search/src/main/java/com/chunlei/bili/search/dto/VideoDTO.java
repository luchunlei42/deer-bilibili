package com.chunlei.bili.search.dto;

import com.chunlei.bili.member.model.Member;
import com.chunlei.bili.search.entity.EsVideo;
import lombok.Data;

import java.util.Date;

@Data
public class VideoDTO extends EsVideo {

    private String nickName;

    private Long view;

    private Long danmakuCount;

    private Long like;

    private Date date;

}

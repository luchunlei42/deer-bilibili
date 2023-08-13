package com.chunlei.bili.search.dto;

import com.chunlei.bili.video.model.Video;
import lombok.Data;

@Data
public class VideoEntity extends VideoDTO {

    private MemberInfo memberInfo;
    private Long view;
    private Long replyCount;
    private Long like;
    private Long danmaku;
    private String playUrl;
}

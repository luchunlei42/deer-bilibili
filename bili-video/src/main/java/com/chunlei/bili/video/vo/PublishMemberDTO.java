package com.chunlei.bili.video.vo;

import lombok.Data;

@Data
public class PublishMemberDTO {

    private String author;

    private Long mid;

    private String avatar;

    private String sign;

    private Integer level;

    private Long fans;

    private Long video;
}

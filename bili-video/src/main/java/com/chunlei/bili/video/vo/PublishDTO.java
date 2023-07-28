package com.chunlei.bili.video.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PublishDTO {

    private Long vid;

    private PublishMemberDTO member;

    private Long typeId;

    private String typename;

    private String videoUrl;

    private String title;

    private String description;

    private String pic;

    private Long play;

    private Long videoReview;

    private Long favorites;

    private List<String> tag;

    private Date pubDate;

    private Date sendDate;

    private String duration;
}

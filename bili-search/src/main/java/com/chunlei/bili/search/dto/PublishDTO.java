package com.chunlei.bili.search.dto;

import com.chunlei.bili.search.entity.EsMember;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

    private List<String> tag;

    private Date pubDate;

    private Date sendDate;

    private String duration;
}

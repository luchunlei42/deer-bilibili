package com.chunlei.bili.search.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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

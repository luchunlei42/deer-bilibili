package com.chunlei.bili.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "member",createIndex = false)
public class EsMember {

    @Field(type = FieldType.Text)
    private String nickname;

    @Id
    @Field(type = FieldType.Keyword)
    private Long mid;

    @Field(type = FieldType.Keyword)
    private String avatar;

    @Field(type = FieldType.Keyword)
    private String sign;

    @Field(type = FieldType.Integer)
    private Integer level;

    @Field(type = FieldType.Long)
    private Long fans;

    @Field(type = FieldType.Long)
    private Long video;
}

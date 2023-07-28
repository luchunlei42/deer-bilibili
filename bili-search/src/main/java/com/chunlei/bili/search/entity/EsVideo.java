package com.chunlei.bili.search.entity;

import jdk.internal.dynalink.linker.LinkerServices;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "video",createIndex = false)
public class EsVideo {
    @Id
    @Field(type = FieldType.Keyword)
    private Long vid;

    @Field(type = FieldType.Nested)
    private EsMember member;

    @Field(type = FieldType.Keyword, name = "typeid")
    private Long typeId;

    @Field(type = FieldType.Keyword)
    private String typename;

    @Field(type = FieldType.Keyword, name = "videourl")
    private String videoUrl;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String pic;

    @Field(type = FieldType.Long)
    private Long play;

    @Field(type = FieldType.Long,name = "video_review")
    private Long videoReview;

    @Field(type = FieldType.Long)
    private Long favorites;

    @Field(type = FieldType.Keyword)
    private List<String> tag;

    @Field(type = FieldType.Date, name = "pubdate", format = DateFormat.epoch_millis)
    private Date pubDate;

    @Field(type = FieldType.Date, name = "senddate", format = DateFormat.epoch_millis)
    private Date sendDate;

    @Field(type = FieldType.Keyword)
    private String duration;

}

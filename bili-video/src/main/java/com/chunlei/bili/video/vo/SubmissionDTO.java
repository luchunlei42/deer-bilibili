package com.chunlei.bili.video.vo;

import lombok.Data;
import org.apache.catalina.LifecycleState;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SubmissionDTO {

    @NotNull
    private String bucketName;

    @NotNull
    private String objectKey;

    @NotNull
    private String location;

    @NotNull
    private Long memberId;

    @NotNull
    private String videoTitle;

    private String videoDescription;

    @NotNull
    private String imgUrl;

    @NotNull
    private Long catalogId;

    @NotNull
    private Integer type;

    @NotNull
    private Integer schedule;

    private List<Long> tagIdList;

    @NotNull
    private Long duration;
}

package com.chunlei.bili.danmaku.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DanmakuRequestEntity {
    private Long player;
    private String author;
    private BigDecimal time;
    private String text;
    private String color;
    private String type;
    private String token;
}

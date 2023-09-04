package com.chunlei.bili.danmaku.dto;

import lombok.Data;

import java.util.List;

@Data
public class DanmakuEntity {
    private Integer code;
    private List<Object[]> danmaku;
}

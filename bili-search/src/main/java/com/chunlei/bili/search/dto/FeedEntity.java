package com.chunlei.bili.search.dto;

import lombok.Data;

import java.util.List;

@Data
public class FeedEntity {
    private List<VideoDTO> videos;
    private Long timestamp;
    private Long last;
}

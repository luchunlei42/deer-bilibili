package com.chunlei.bili.search.service;

import com.chunlei.bili.search.dto.OperationDTO;
import com.chunlei.bili.search.dto.VideoDTO;

import java.util.List;

public interface RecommendService {
    void addOperation(OperationDTO operationDTO);

    List<VideoDTO> getRecommendVideoByUser(Long memberId, Integer ps);

    List<VideoDTO> getRecommendVideoByItem(Long memberId, Long videoId, Integer ps);
}

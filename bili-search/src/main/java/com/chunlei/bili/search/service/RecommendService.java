package com.chunlei.bili.search.service;

import com.chunlei.bili.search.dto.OperationDTO;
import com.chunlei.bili.search.entity.EsVideo;

import java.util.List;

public interface RecommendService {
    void addOperation(OperationDTO operationDTO);

    List<EsVideo> getRecommendVideoByUser(Long memberId, Integer ps);

    List<EsVideo> getRecommendVideoByItem(Long memberId, Long videoId, Integer ps);
}

package com.chunlei.bili.search.service;

import com.chunlei.bili.search.dto.FeedEntity;
import com.chunlei.bili.search.dto.PublishDTO;
import com.chunlei.bili.search.dto.VideoDTO;
import com.chunlei.bili.search.dto.VideoEntity;
import com.chunlei.bili.search.entity.EsVideo;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EsSearchService {

    void save(PublishDTO publishDTO);

    List<VideoDTO> searchByRegion(Long rid, Integer ps, Integer pn) throws IOException, ExecutionException, InterruptedException;

    List<VideoDTO> findVideoByIds(List<Long> videoIdList) throws ExecutionException, InterruptedException;

    VideoEntity getVideoEntity(Long videoId) throws ExecutionException, InterruptedException;

    FeedEntity feed(Integer ps, Long timestamp, Long last) throws IOException, ExecutionException, InterruptedException;

    List<VideoDTO> searchByKeyword(String keyword, String type, Integer page) throws IOException, ExecutionException, InterruptedException;
}

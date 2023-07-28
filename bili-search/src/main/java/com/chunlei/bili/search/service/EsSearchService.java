package com.chunlei.bili.search.service;

import com.chunlei.bili.search.dto.PublishDTO;
import com.chunlei.bili.search.entity.EsVideo;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface EsSearchService {

    void save(PublishDTO publishDTO);

    List<EsVideo> searchByRegion(Long rid, Integer ps, Integer pn) throws IOException;
}

package com.chunlei.bili.search.repository;

import com.chunlei.bili.search.entity.EsVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsVideoRepository extends ElasticsearchRepository<EsVideo, Long> {

    /**
     * 根据分区进行搜索
     * @param typeId
     * @param pageable
     * @return
     */
    Page<EsVideo> findByTypeId(Long typeId, Pageable pageable);

    EsVideo findByTypeId(Long typeId);

    @Query("{\"match\":{\"typeid\":\"?0\"}}")
    Page<EsVideo> find(Long typeId, Pageable pageable);

    List<EsVideo> findEsVideosByVid(List<Long> vidList);
}

package com.chunlei.bili.search.repository;

import com.chunlei.bili.search.entity.EsMember;
import com.chunlei.bili.search.entity.EsVideo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsMemberRepository extends ElasticsearchRepository<EsMember, Long> {

}

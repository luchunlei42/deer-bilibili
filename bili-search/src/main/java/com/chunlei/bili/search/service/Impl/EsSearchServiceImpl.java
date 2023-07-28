package com.chunlei.bili.search.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.chunlei.bili.search.dto.PublishDTO;
import com.chunlei.bili.search.entity.EsMember;
import com.chunlei.bili.search.entity.EsVideo;
import com.chunlei.bili.search.repository.EsVideoRepository;
import com.chunlei.bili.search.service.EsSearchService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chunlei.bili.search.config.EsRestClientConfig.VIDEO_INDEX;

@Service
public class EsSearchServiceImpl implements EsSearchService {
    @Autowired
    EsVideoRepository videoRepository;
    @Autowired
    RestHighLevelClient esClient;

    @Override
    public void save(PublishDTO publishDTO) {
        EsVideo video = new EsVideo();
        EsMember member = new EsMember();
        BeanUtil.copyProperties(publishDTO.getMember(),member);
        BeanUtil.copyProperties(publishDTO,video);
        video.setMember(member);

        videoRepository.save(video);
    }

    @Override
    public List<EsVideo> searchByRegion(Long rid, Integer ps, Integer pn) throws IOException {
        //Pageable pageable = PageRequest.of(pn,ps, Sort.Direction.DESC,"pubdate");
        //return videoRepository.findByTypeId(rid,pageable);

        List<EsVideo> res = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("typeid", rid);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(pn);
        searchSourceBuilder.size(ps);

        SearchRequest searchRequest = new SearchRequest(VIDEO_INDEX);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits){
            String sourceAsString = hit.getSourceAsString();
            EsVideo video = JSON.parseObject(sourceAsString, EsVideo.class);
            res.add(video);
        }
        return res;
    }
}

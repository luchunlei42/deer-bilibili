package com.chunlei.bili.search.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.search.client.DanmakuFeignClient;
import com.chunlei.bili.search.client.MemberFeignClient;
import com.chunlei.bili.search.client.VideoFeignClient;
import com.chunlei.bili.search.dto.*;
import com.chunlei.bili.search.entity.EsMember;
import com.chunlei.bili.search.entity.EsVideo;
import com.chunlei.bili.search.repository.EsMemberRepository;
import com.chunlei.bili.search.repository.EsVideoRepository;
import com.chunlei.bili.search.service.EsSearchService;
import com.chunlei.bili.video.model.VideoDetail;
import com.chunlei.bili.video.model.VideoStat;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.chunlei.bili.search.config.EsRestClientConfig.VIDEO_INDEX;

@Service
@Slf4j
public class EsSearchServiceImpl implements EsSearchService {
    @Autowired
    EsVideoRepository videoRepository;
    @Autowired
    RestHighLevelClient esClient;
    @Autowired
    EsMemberRepository memberRepository;
    @Autowired
    MemberFeignClient memberFeignClient;
    @Autowired
    ExecutorService taskThreadPool;
    @Autowired
    ExecutorService memberInfoThreadPool;
    @Autowired
    ExecutorService videoStatThreadPool;
    @Autowired
    DanmakuFeignClient danmakuFeignClient;
    @Autowired
    VideoFeignClient videoFeignClient;

    @Override
    public void save(PublishDTO publishDTO) {
        EsVideo video = new EsVideo();
        EsMember member = new EsMember();
        BeanUtil.copyProperties(publishDTO.getMember(),member);
        member.setNickname(publishDTO.getMember().getAuthor());
        BeanUtil.copyProperties(publishDTO,video);
        video.setMid(member.getMid());
        memberRepository.save(member);
        videoRepository.save(video);
        log.info("publish video:{}, member:{}",JSON.toJSON(video),JSON.toJSON(member));
    }

    @Override
    public List<VideoDTO> searchByRegion(Long rid, Integer ps, Integer pn) throws IOException, ExecutionException, InterruptedException {
//        Pageable pageable = PageRequest.of(pn,ps, Sort.Direction.DESC,"pubdate");
//        return videoRepository.findByTypeId(rid);

        List<EsVideo> res = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("typeid", rid);
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((pn-1)*ps);
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
        List<VideoDTO> videoDTOList = mappingEsVideoToVideoDTO(res);
        return videoDTOList;
    }

    @Override
    public List<VideoDTO> findVideoByIds(List<Long> videoIdList) throws ExecutionException, InterruptedException {
        List<EsVideo> list = new ArrayList<>();
        Iterable<EsVideo> all = videoRepository.findAllById(videoIdList);
        all.forEach(single->{list.add(single);});
        return mappingEsVideoToVideoDTO(list);
    }

    @Override
    public VideoEntity getVideoEntity(Long videoId) throws ExecutionException, InterruptedException {
        VideoEntity video = new VideoEntity();
        List<EsVideo> list = new ArrayList<>();
        Iterable<EsVideo> all = videoRepository.findAllById(Collections.singletonList(videoId));
        all.forEach(single->{list.add(single);});
        EsVideo esVideo = list.get(0);
        video = mappingEsVideoToVideoEntity(esVideo);
        return video;
    }

    @Override
    public FeedEntity feed(Integer ps, Long timestamp, Long last) throws IOException, ExecutionException, InterruptedException {
        FeedEntity feedEntity = new FeedEntity();
        List<EsVideo> res = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(ps);
        FieldSortBuilder pubdate = SortBuilders.fieldSort("pubdate").order(SortOrder.DESC);
        FieldSortBuilder vid = SortBuilders.fieldSort("vid");
        searchSourceBuilder.sort(pubdate).sort(vid);
        if (timestamp != null || last != null){
            searchSourceBuilder.searchAfter(new Object[]{timestamp,last});
        }
        SearchRequest searchRequest = new SearchRequest(VIDEO_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (int i = 0; i < searchHits.length; i++) {
            SearchHit hit = searchHits[i];
            String sourceAsString = hit.getSourceAsString();
            EsVideo video = JSON.parseObject(sourceAsString, EsVideo.class);
            res.add(video);
            if (i == searchHits.length-1){
                feedEntity.setLast(video.getVid());
                feedEntity.setTimestamp(video.getPubDate().getTime());
            }
        }
        List<VideoDTO> videoDTOList = mappingEsVideoToVideoDTO(res);
        feedEntity.setVideos(videoDTOList);
        return feedEntity;
    }

    @Override
    public List<VideoDTO> searchByKeyword(String keyword, String type, Integer page) throws IOException, ExecutionException, InterruptedException {
        int ps = 20;
        List<EsVideo> res = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "tags", "typename");

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((page-1)*ps);
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
        List<VideoDTO> videoDTOList = mappingEsVideoToVideoDTO(res);
        return videoDTOList;
    }

    private VideoEntity mappingEsVideoToVideoEntity(EsVideo esVideo) {
        VideoEntity video = new VideoEntity();
        BeanUtil.copyProperties(esVideo,video);
        CompletableFuture<Void> memberInfoFuture = CompletableFuture.runAsync(() -> {
            R<MemberInfo> memberInfoR = memberFeignClient.memberInfo(esVideo.getMid());
            if (memberInfoR.getCode() == 200) {
                video.setMemberInfo(memberInfoR.getData());
                video.setNickName(video.getMemberInfo().getNickName());
            }
        }, memberInfoThreadPool);
        CompletableFuture<Void> videoStatFuture = CompletableFuture.runAsync(()->{
            Long danmakuCount = danmakuFeignClient.getDanmakuCount(esVideo.getVid());
            video.setDanmakuCount(danmakuCount);
            VideoStat videoStat = videoFeignClient.getVideoStat(video.getVid());
            video.setView(videoStat.getView());
            video.setLike(videoStat.getLike());
            video.setReplyCount(videoStat.getReply());
        }, videoStatThreadPool);
        VideoDetail videoDetail = videoFeignClient.getVideoDetail(video.getVid());
        video.setPlayUrl(videoDetail.getVideoKey());
        CompletableFuture.allOf(memberInfoFuture,videoStatFuture).join();
        return video;
    }

    public List<VideoDTO> mappingEsVideoToVideoDTO(List<EsVideo> esVideos) throws ExecutionException, InterruptedException {
        List<CompletableFuture<VideoDTO>> futureList = esVideos.stream().map(esVideo -> getVideoDTOFuture(esVideo)).collect(Collectors.toList());
        CompletableFuture<Void> all = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        CompletableFuture<List<VideoDTO>> listCompletableFuture = all.thenApply(v -> futureList.stream().map(a -> {
            try {
                return a.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
        List<VideoDTO> videoDTOList = listCompletableFuture.get();
        return videoDTOList;
    }

    private CompletableFuture<VideoDTO> getVideoDTOFuture(EsVideo esVideo) {
        VideoDTO videoDTO = new VideoDTO();
        BeanUtil.copyProperties(esVideo,videoDTO);
        CompletableFuture<Void> memberInfoFuture = CompletableFuture.runAsync(() -> {
            R<MemberInfo> memberInfoR = memberFeignClient.memberInfo(esVideo.getMid());
            if (memberInfoR.getCode() == 200) {
                videoDTO.setNickName(memberInfoR.getData().getNickName());
            }
        }, memberInfoThreadPool);
        CompletableFuture<Void> videoStatFuture = CompletableFuture.runAsync(()->{
            Long danmakuCount = danmakuFeignClient.getDanmakuCount(esVideo.getVid());
            videoDTO.setDanmakuCount(danmakuCount);
            Long videoView = videoFeignClient.getVideoView(videoDTO.getVid());
            videoDTO.setView(videoView);
        }, videoStatThreadPool);
        return CompletableFuture.allOf(memberInfoFuture,videoStatFuture).thenApplyAsync(x-> videoDTO,taskThreadPool);
    }
}

package com.chunlei.bili.search.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.search.dto.FeedEntity;
import com.chunlei.bili.search.dto.PublishDTO;
import com.chunlei.bili.search.dto.VideoDTO;
import com.chunlei.bili.search.dto.VideoEntity;
import com.chunlei.bili.search.entity.EsVideo;
import com.chunlei.bili.search.service.EsSearchService;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class SearchController {

    @Autowired
    private EsSearchService searchService;

    @PostMapping("/publish")
    public R publishVideo(@RequestBody PublishDTO publishDTO){
        searchService.save(publishDTO);
        return R.success(null);
    }

    @GetMapping("/region")
    public R searchByRegion(@RequestParam("rid") Long rid, @RequestParam("ps") Integer ps, @RequestParam("pn") Integer pn) throws IOException, ExecutionException, InterruptedException {
        List<VideoDTO> r =  searchService.searchByRegion(rid,ps,pn);
        return R.success(r);
    }

    @GetMapping("/detail/{videoId}")
    public R getVideoById(@PathVariable("videoId") Long videoId) throws ExecutionException, InterruptedException {
        if (videoId == null) return R.failed();
        VideoEntity video = searchService.getVideoEntity(videoId);
        return R.success(video);
    }

    @GetMapping("/feed")
    public R getMainFeed(@RequestParam("ps") Integer ps, @RequestParam(value = "timestamp",required = false) Long timestamp, @RequestParam(value = "last",required = false)Long last) throws IOException, ExecutionException, InterruptedException {
        if (ps == null) {
            return R.failed();
        }
        FeedEntity feedEntity = searchService.feed(ps, timestamp, last);
        return R.success(feedEntity);
    }

    @GetMapping("/type")
    public R searchByKeyword(@RequestParam("keyword") String keyword,@RequestParam("search_type")String type,@RequestParam("page") Integer page) throws IOException, ExecutionException, InterruptedException {
        List<VideoDTO> r = searchService.searchByKeyword(keyword,type,page);
        return R.success(r);
    }
}

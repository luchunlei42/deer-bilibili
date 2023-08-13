package com.chunlei.bili.video.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.model.VideoStat;
import com.chunlei.bili.video.service.VideoStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutorService;

@RestController
public class VideoStatController {
    @Autowired
    VideoStatService videoStatService;
    @Autowired
    ExecutorService fetchVideoThreadPool;

    @PostMapping("/stat/reply")
    public R incrementReplyCount(@RequestBody Map<Long,Long> map){
        if (map == null || map.isEmpty()){
            return R.success(null);
        }
        videoStatService.incrementReplyCount(map);
        return R.success(null);
    }

    @GetMapping("/stat/view/{videoId}")
    public Long getVideoView(@PathVariable("videoId") Long videoId){
        return videoStatService.getVideoView(videoId);
    }

    @GetMapping("/stat/{videoId}")
    public VideoStat getVideoStat(@PathVariable("videoId") Long videoId){
        return videoStatService.getVideoStat(videoId);
    }

}

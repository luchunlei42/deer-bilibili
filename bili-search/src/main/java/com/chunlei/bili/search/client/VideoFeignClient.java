package com.chunlei.bili.search.client;

import com.chunlei.bili.video.model.VideoDetail;
import com.chunlei.bili.video.model.VideoStat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bili-video",path = "apix/video")
public interface VideoFeignClient {

    @GetMapping("/stat/view/{videoId}")
    public Long getVideoView(@PathVariable("videoId") Long videoId);

    @GetMapping("/stat/{videoId}")
    public VideoStat getVideoStat(@PathVariable("videoId") Long videoId);

    @GetMapping("/detail")
    public VideoDetail getVideoDetail(@RequestParam("videoId") Long videoId);
}

package com.chunlei.bili.reply.client;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.model.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("bili-video")
public interface VideoFeignClient {

    @GetMapping("/info/{videoId}")
    R<Video> getVideoInfo(@PathVariable("videoId") Long videoId);

    @PostMapping("/stat/reply")
    public R incrementReplyCount(@RequestBody Map<Long,Long> map);
}

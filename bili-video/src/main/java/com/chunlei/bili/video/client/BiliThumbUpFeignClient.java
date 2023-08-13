package com.chunlei.bili.video.client;

import com.chunlei.bili.common.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "bili-thumbup", path = "apix/thumbup")
public interface BiliThumbUpFeignClient {
    @GetMapping("/like/{videoId}")
    public R<Long> getLikeByVideoId(@PathVariable("videoId") Long videoId);
}

package com.chunlei.bili.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "bili-danmaku", path = "apix/danmaku")
public interface DanmakuFeignClient {

    @GetMapping("/count/{videoId}")
    public Long getDanmakuCount(@PathVariable("videoId")Long videoId);
}

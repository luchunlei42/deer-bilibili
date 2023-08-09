package com.chunlei.bili.thumbup.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bili-video",path = "apix/video")
public interface VideoFeignClient {
}

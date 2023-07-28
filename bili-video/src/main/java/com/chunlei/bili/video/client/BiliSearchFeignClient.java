package com.chunlei.bili.video.client;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.vo.PublishDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "bili-search",path = "apix/search")
public interface BiliSearchFeignClient {

    @PostMapping("/publish")
    R<PublishDTO> publishVideo(@RequestBody PublishDTO publishDTO);
}

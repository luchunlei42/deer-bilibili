package com.chunlei.bili.video.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.service.VideoStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class VideoStatController {
    @Autowired
    VideoStatService videoStatService;

    @PostMapping("/stat/reply")
    public R incrementReplyCount(@RequestBody Map<Long,Long> map){
        if (map == null || map.isEmpty()){
            return R.success(null);
        }
        videoStatService.incrementReplyCount(map);
        return R.success(null);
    }
}

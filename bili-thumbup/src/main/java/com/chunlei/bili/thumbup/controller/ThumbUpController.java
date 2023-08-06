package com.chunlei.bili.thumbup.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.thumbup.model.Like;
import com.chunlei.bili.thumbup.service.ThumbUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
public class ThumbUpController {
    @Autowired
    ThumbUpService thumbUpService;

    @PostMapping("/like")
    public R like(@RequestBody Like like){
        if (like == null ||  like.getLikedPostId() == null || like.getStatus() == null){
            return R.failed("缺少参数");
        }
        thumbUpService.like(like);
        return R.success(null);
    }

    @GetMapping("/like/{videoId}")
    public R getLikeByVideoId(@PathVariable("videoId") Long videoId){
        if (videoId == null){
            return R.failed("不能没有videoId");
        }
        Long count = thumbUpService.getLikeByVideoId(videoId);
        return R.success(count);
    }


}

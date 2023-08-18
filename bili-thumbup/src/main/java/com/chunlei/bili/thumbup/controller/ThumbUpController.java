package com.chunlei.bili.thumbup.controller;

import cn.hutool.core.util.StrUtil;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.JwtUtils;
import com.chunlei.bili.thumbup.model.Like;
import com.chunlei.bili.thumbup.service.ThumbUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.chunlei.bili.common.constant.WebConstants.TOKEN_HEAD;

@RestController
@Slf4j
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

    @GetMapping("/isLike")
    public R isLike(@RequestParam("videoId") Long videoId, @RequestParam("memberId") Long memberId, HttpServletRequest request){
        if (videoId == null){
            return R.failed("不能没有videoId");
        }
        String token = request.getHeader("Authorization");
        log.info("token is :{}",token);
        if (!StrUtil.isBlank(token) && token.startsWith(TOKEN_HEAD)){
            token = token.substring(TOKEN_HEAD.length());
            boolean verify = JwtUtils.checkToken(token);
            if (verify){
                String id = JwtUtils.getId(token);
                if (memberId == 0){
                    memberId = Long.valueOf(id);
                }
            }
        }
        log.info("memberId:{}",memberId);
        Boolean isLike = thumbUpService.isLike(videoId,memberId);
        return R.success(isLike);
    }
}

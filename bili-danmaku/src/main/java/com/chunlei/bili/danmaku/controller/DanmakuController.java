package com.chunlei.bili.danmaku.controller;

import com.alibaba.fastjson.JSON;
import com.chunlei.bili.common.utils.JwtUtils;
import com.chunlei.bili.danmaku.dto.DanmakuEntity;
import com.chunlei.bili.danmaku.dto.DanmakuRequestEntity;
import com.chunlei.bili.danmaku.model.Danmaku;
import com.chunlei.bili.danmaku.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.chunlei.bili.common.constant.WebConstants.TOKEN_HEAD;

@RestController
public class DanmakuController {
    @Autowired
    DanmakuService danmakuService;

    @GetMapping("/count/{videoId}")
    public Long getDanmakuCount(@PathVariable("videoId")Long videoId){
        return danmakuService.getDanmakuCount(videoId);
    }

    @GetMapping("/v2")
    public DanmakuEntity getDanmaku(@RequestParam("id")Long vid){
        DanmakuEntity danmakuEntity = new DanmakuEntity();
        danmakuEntity.setCode(0);
        List<Object[]> danmakus = danmakuService.getDanmaku(vid);
        danmakuEntity.setDanmaku(danmakus);
        return danmakuEntity;
    }

    @PostMapping("/v2")
    public DanmakuEntity postDanmaku(@RequestBody DanmakuRequestEntity danmakuRequestEntity){
        String token = danmakuRequestEntity.getToken();
        Long memberId = null;
        try {
            token = token.substring(TOKEN_HEAD.length());
            memberId = Long.valueOf(JwtUtils.getId(token));
        } catch (Exception ignored){
        }
        Danmaku danmaku = new Danmaku();
        danmaku.setMemberId(memberId);
        danmaku.setCreateTime(new Date());
        danmaku.setDanmakuTime(danmakuRequestEntity.getTime().toString());
        danmaku.setContent(danmakuRequestEntity.getText());
        danmaku.setVideoId(danmakuRequestEntity.getPlayer());
        danmaku.setColor(danmakuRequestEntity.getColor());
        danmaku.setAuthor(danmakuRequestEntity.getAuthor());
        danmaku.setType(1);
        danmakuService.addDanmaku(danmaku);
        danmakuService.addDanmakuToRedis(danmaku);
        DanmakuEntity danmakuEntity = new DanmakuEntity();
        danmakuEntity.setCode(0);
        return danmakuEntity;
    }
}

package com.chunlei.bili.danmaku.controller;

import com.chunlei.bili.danmaku.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DanmakuController {
    @Autowired
    DanmakuService danmakuService;

    @GetMapping("/count/{videoId}")
    public Long getDanmakuCount(@PathVariable("videoId")Long videoId){
        return danmakuService.getDanmakuCount(videoId);
    }
}

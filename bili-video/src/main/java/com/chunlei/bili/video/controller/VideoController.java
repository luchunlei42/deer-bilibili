package com.chunlei.bili.video.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.service.VideoService;
import com.chunlei.bili.video.vo.SubmissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
public class VideoController {
    @Autowired
    VideoService videoService;

    /**
     * 投稿
     * @param dto
     * @return
     */
    @PostMapping("/upload/frame")
    public R uploadVideo(@Valid @RequestBody SubmissionDTO dto){
        try {
            videoService.uploadVideo(dto);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        } catch (InterruptedException e) {
            return R.failed(e.getMessage());
        }
        return R.success(null);
    }

    /**
     * 发布
     * @param videoId
     * @return
     */
    @PostMapping("/publish")
    public R publishVideo(@RequestParam Long videoId){
        return videoService.publish(videoId);
    }


}

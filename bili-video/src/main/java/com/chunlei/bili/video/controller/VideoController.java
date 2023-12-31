package com.chunlei.bili.video.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.constant.FilePath;
import com.chunlei.bili.video.model.Video;
import com.chunlei.bili.video.model.VideoDetail;
import com.chunlei.bili.video.service.VideoService;
import com.chunlei.bili.video.vo.SubmissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class VideoController {
    @Autowired
    VideoService videoService;
    @Autowired
    FilePath filePath;

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
    
    @PostMapping("/publish/all")
    public R publishAll(){
        for (int i = 1; i < 396; i++) {
            videoService.publish((long) i);
        }
        return R.success(null);
    }

    @GetMapping("/info/{videoId}")
    public R getVideoInfo(@PathVariable("videoId") Long videoId){
        if (videoId == null) return R.failed();
        Video video = videoService.findVideoById(videoId);
        if (video == null){
            return R.failed();
        }
        return R.success(video);
    }

    @GetMapping("/play")
    public void playVideo(@RequestParam("videoId") Long videoId,
                                                         HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        videoService.playVideo(videoId, request, response);
    }

    @GetMapping("/playUrl")
    public R<String> playVideoUrl(@RequestParam("k") String k){
        String playUrl = videoService.getVideoPlayUrl(k);
        return R.success(playUrl);
    }

    @GetMapping("/detail")
    public VideoDetail getVideoDetail(@RequestParam("videoId") Long videoId){
        VideoDetail videoDetail =  videoService.getVideoDetail(videoId);
        return videoDetail;
    }

}

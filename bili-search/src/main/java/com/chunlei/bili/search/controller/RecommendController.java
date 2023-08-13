package com.chunlei.bili.search.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.search.dto.OperationDTO;
import com.chunlei.bili.search.dto.VideoDTO;
import com.chunlei.bili.search.entity.EsVideo;
import com.chunlei.bili.search.service.RecommendService;
import com.chunlei.bili.video.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import java.util.List;

@RestController
public class RecommendController {
    @Autowired
    RecommendService recommendService;

    @PostMapping("/opt")
    public R addOperation(@RequestBody OperationDTO operationDTO){
        recommendService.addOperation(operationDTO);
        return R.success(null);
    }

    @GetMapping("/recommend")
    public R getRecommendVideo(@RequestParam(value = "memberId",required = false) Long memberId,
                                     @RequestParam(value = "videoId", required = false) Long videoId,
                                     @RequestParam(value = "ps",defaultValue = "5") Integer ps){
        List<VideoDTO> videos = null;
        if (videoId == null){
            //基于用户推荐视频
            videos = recommendService.getRecommendVideoByUser(memberId,ps);
        } else {
            //基于视频推荐视频
            videos = recommendService.getRecommendVideoByItem(memberId,videoId,ps);
        }
        return R.success(videos);
    }


}

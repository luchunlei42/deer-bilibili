package com.chunlei.bili.video.service;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.model.Video;
import com.chunlei.bili.video.vo.SubmissionDTO;

import java.util.concurrent.ExecutionException;

public interface VideoService {
    void uploadVideo(SubmissionDTO dto) throws ExecutionException, InterruptedException;

    Video findVideoById(Long videoId);

    R publish(Long videoId);
}

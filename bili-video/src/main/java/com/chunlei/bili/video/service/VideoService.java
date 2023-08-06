package com.chunlei.bili.video.service;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.model.Video;
import com.chunlei.bili.video.vo.SubmissionDTO;
import org.springframework.core.io.InputStreamResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface VideoService {
    void uploadVideo(SubmissionDTO dto) throws ExecutionException, InterruptedException;

    Video findVideoById(Long videoId);

    R publish(Long videoId);

    void playVideo(Long videoId, HttpServletRequest request, HttpServletResponse response) throws IOException;
}

package com.chunlei.bili.video.service;

import com.chunlei.bili.video.vo.SubmissionDTO;

import java.util.concurrent.ExecutionException;

public interface VideoService {
    void uploadVideo(SubmissionDTO dto) throws ExecutionException, InterruptedException;
}

package com.chunlei.bili.video.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    private final static int THREAD_SIZE = 8;

    @Bean
    public Executor getUploadThreadPool(){
        return Executors.newFixedThreadPool(THREAD_SIZE);
    }
}

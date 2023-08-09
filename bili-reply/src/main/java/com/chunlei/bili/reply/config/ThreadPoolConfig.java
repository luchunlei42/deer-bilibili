package com.chunlei.bili.reply.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService childrenReplyThreadPool(){
        return new ThreadPoolExecutor(8,8,30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(8));
    }
}

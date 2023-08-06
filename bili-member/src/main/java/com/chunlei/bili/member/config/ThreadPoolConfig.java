package com.chunlei.bili.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    private static final Integer ChildrenReplyCoreThreadPool = 15;
    private static final Integer ChildrenReplyMaxThreadPool = 20;
    private static final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);

    @Bean
    public ExecutorService childrenReplyThreadPool(){
        return new ThreadPoolExecutor(ChildrenReplyCoreThreadPool,ChildrenReplyMaxThreadPool,30, TimeUnit.SECONDS,queue);
    }

}

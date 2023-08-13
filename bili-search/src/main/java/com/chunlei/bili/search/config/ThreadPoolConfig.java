package com.chunlei.bili.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService memberInfoThreadPool(){
        return Executors.newFixedThreadPool(5);
    }

    @Bean
    public ExecutorService videoStatThreadPool(){
        return Executors.newFixedThreadPool(5);
    }

    @Bean
    public ExecutorService taskThreadPool(){
        return Executors.newFixedThreadPool(5);
    }

}

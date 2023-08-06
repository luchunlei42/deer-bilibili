package com.chunlei.bili.danmaku.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public static final String DANMAKU_TOPIC = "danmaku_launch";
    public static final String SEND_TOPIC = "danmaku_SEND";
}

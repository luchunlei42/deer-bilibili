package com.chunlei.bili.reply.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    public static final String REPLY_INDEX_PREFIX = "reply:index:";

    public static final Integer REPLY_INDEX_EXPIRE = 24;

    public static final String REPLY_CONTENT_PREFIX = "reply:index:";

    public static final Integer REPLY_CONTENT_EXPIRE = 24;

}

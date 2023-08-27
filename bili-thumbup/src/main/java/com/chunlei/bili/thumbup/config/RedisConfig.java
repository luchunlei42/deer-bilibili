package com.chunlei.bili.thumbup.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    public static final String LIKE_PREFIX = "user:like:";

    public static final String LIKE_COUNT_PREFIX = "like:count:";

    public static final String UPDATE_LIKE_PREFIX = "db:like:";

    public static final String UPDATE_NOTLIKE_PREFIX = "db:notlike:";

    public static final String UPDATE_LIKE_COUNT_PREFIX = "db:likecount:";

    public static final Integer shards = 20;

    public static final Integer LIKE_EXPIRE = 60;

    public static final Integer LIKE_COUNT_EXPIRE = 60;

    public static final String LIKE_BLOOM = "like:bloom:";
}

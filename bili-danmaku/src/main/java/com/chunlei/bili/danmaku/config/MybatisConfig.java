package com.chunlei.bili.danmaku.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.danmaku.mapper","com.chunlei.bili.danmaku.dao"})
public class MybatisConfig {
}

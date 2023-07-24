package com.chunlei.bili.video.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.video.mapper","com.chunlei.bili.video.dao"})
public class MybatisConfig {
}

package com.chunlei.bili.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.auth.mapper"})
public class MybatisConfig {
}

package com.chunlei.bili.thumbup.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.thumbup.mapper","com.chunlei.bili.thumbup.dao"})
public class MybatisConfig {
}

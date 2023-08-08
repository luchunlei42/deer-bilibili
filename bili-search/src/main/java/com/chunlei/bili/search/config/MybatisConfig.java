package com.chunlei.bili.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.search.mapper","com.chunlei.bili.search.dao"})
public class MybatisConfig {
}

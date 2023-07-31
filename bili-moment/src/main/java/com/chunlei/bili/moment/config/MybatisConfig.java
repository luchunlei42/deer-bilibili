package com.chunlei.bili.moment.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.moment.mapper","com.chunlei.bili.moment.dao"})
public class MybatisConfig {
}

package com.chunlei.bili.reply.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.reply.mapper","com.chunlei.bili.reply.dao"})
public class MybatisConfig {
}

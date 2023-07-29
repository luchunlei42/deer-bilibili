package com.chunlei.bili.member.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.bili.member.mapper","com.chunlei.bili.member.dao"})
public class MybatisConfig {
}

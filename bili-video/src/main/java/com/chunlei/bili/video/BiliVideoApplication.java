package com.chunlei.bili.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableFeignClients
public class BiliVideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiliVideoApplication.class,args);
    }
}

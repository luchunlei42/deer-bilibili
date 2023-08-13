package com.chunlei.bili.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication()
@EnableFeignClients
public class BiliSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiliSearchApplication.class, args);
    }
}

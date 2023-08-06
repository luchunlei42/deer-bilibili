package com.chunlei.bili.thumbup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BiliThumbUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiliThumbUpApplication.class,args);
    }
}

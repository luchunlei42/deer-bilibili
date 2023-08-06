package com.chunlei.bili.reply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BiliReplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiliReplyApplication.class,args);
    }
}

package com.chunlei.bili.moment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BiliMomentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiliMomentApplication.class,args);
    }
}

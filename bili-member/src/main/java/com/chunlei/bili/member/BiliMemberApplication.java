package com.chunlei.bili.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BiliMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiliMemberApplication.class,args);
    }
}

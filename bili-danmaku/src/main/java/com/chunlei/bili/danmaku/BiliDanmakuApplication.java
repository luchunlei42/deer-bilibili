package com.chunlei.bili.danmaku;

import com.chunlei.bili.danmaku.service.WebSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BiliDanmakuApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BiliDanmakuApplication.class, args);
        WebSocketService.setApplicationContext(context);
    }
}

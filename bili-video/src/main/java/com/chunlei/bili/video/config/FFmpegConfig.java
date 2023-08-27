package com.chunlei.bili.video.config;

import lombok.SneakyThrows;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FFmpegConfig {

    public static final String FFMPEG_PATH = "D:\\Software\\ffmpeg-6.0-essentials_build\\bin\\ffmpeg.exe";
    public static final String FFPROBE_PATH = "D:\\Software\\ffmpeg-6.0-essentials_build\\bin\\ffprobe.exe";

    @Bean
    @SneakyThrows
    public FFmpeg fFmpeg(){
        return new FFmpeg(FFmpeg.FFMPEG);
    }

    @Bean
    @SneakyThrows
    public FFprobe fFprobe(){
        return new FFprobe(FFPROBE_PATH);
    }
}

package com.chunlei.bili.video.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ffpmeg.path")
public class FilePath {


    private String tempPath;

    private String basePath;
}

package com.chunlei.bili.moment.component;

import com.chunlei.bili.common.utils.IdGeneratorSnowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    @Bean
    public IdGeneratorSnowflake idGeneratorSnowflake(){
        return new IdGeneratorSnowflake();
    }
}

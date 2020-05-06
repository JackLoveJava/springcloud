package com.jack.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    /**
     * 日志设置
     * @return
     */
    @Bean
    Logger.Level level() {
        return Logger.Level.FULL;
    }

}

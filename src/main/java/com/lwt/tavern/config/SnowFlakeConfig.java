package com.lwt.tavern.config;

import com.lwt.tavern.common.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfig {

    @Bean("userSnowFlake")
    public SnowFlake userSnowFlake() {
        return new SnowFlake(1,1);
    }
}

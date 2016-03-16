package com.surrey.com3014.group5.configs;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aung Thu Moe
 */
@Configuration
public class AppConfig {
    @Bean
    public ServerProperties getServerProperties() {
        return new ServerCustomization();
    }
}

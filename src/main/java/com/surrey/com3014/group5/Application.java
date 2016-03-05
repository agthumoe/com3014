package com.surrey.com3014.group5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Main application
 *
 * @author Aung Thu Moe
 */
@ComponentScan
@EnableAutoConfiguration
@PropertySource(value = {"classpath:META-INF/resources/properties/application.properties", "classpath:META-INF/resources/properties/database.properties"})
public class Application extends SpringBootServletInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        LOGGER.info("starting tron application");
        SpringApplication.run(Application.class, args);
    }

}

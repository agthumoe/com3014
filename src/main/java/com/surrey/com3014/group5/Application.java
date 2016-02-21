package com.surrey.com3014.group5;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by aungthumoe on 21/02/2016.
 */
@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        // testing logging
        logger.info("starting tron application");
        SpringApplication.run(Application.class, args);
    }
}

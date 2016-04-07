package com.surrey.com3014.group5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;

/**
 * Main application
 *
 * @author Aung Thu Moe
 */
@ComponentScan
@EnableAutoConfiguration
@PropertySource(value = {"classpath:META-INF/application.properties", "classpath:META-INF/database.properties", "classpath:META-INF/swagger.properties"})
public class Application extends SpringBootServletInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        LOGGER.info("starting tron application");
        Environment env = SpringApplication.run(Application.class, args).getEnvironment();

        LOGGER.info("Access URLs:\t\thttp://127.0.0.1:8080");
        LOGGER.info("Active Profiles:\t\t{}", StringUtils.arrayToDelimitedString(env.getActiveProfiles(), ", "));
    }

}

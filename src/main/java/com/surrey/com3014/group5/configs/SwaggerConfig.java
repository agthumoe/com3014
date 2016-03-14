package com.surrey.com3014.group5.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
/**
 * @author Aung Thu Moe
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    Environment env;

    @Bean
    public Docket api() {
        ApiInfo apiInfo = new ApiInfo(
            env.getProperty("swagger.api.name"),
            env.getProperty("swagger.api.description"),
            env.getProperty("swagger.api.version"),
            null,
            null,
            null,
            null);
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(regex("/api/.*"))
            .build();
    }
}

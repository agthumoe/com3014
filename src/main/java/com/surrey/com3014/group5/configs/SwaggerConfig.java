package com.surrey.com3014.group5.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(500).message("Internal Server Error").responseModel(new ModelRef("ErrorDTO")).build());
        responseMessages.add(new ResponseMessageBuilder().code(401).message("Unauthorized").responseModel(new ModelRef("ErrorDTO")).build());
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, newArrayList(new ResponseMessageBuilder().code(500).message("Internal Server Error").responseModel(new ModelRef("ErrorDTO")).build()))
            .globalResponseMessage(RequestMethod.POST, responseMessages)
            .globalResponseMessage(RequestMethod.DELETE, responseMessages)
            .globalResponseMessage(RequestMethod.PUT, responseMessages)
            .apiInfo(apiInfo)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(regex("/api/.*"))
            .build();
    }
}

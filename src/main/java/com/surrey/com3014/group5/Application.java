package com.surrey.com3014.group5;

import com.surrey.com3014.group5.daos.UserDao;
import com.surrey.com3014.group5.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by aungthumoe on 21/02/2016.
 */
@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        // testing logging
        logger.info("starting tron application");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner userInitialisation(UserDao userDao) {
        return (args) -> {
            // save a User
            userDao.save(new User("autoUsername", "autoPassword", "autoEmail", "autoName"));

            logger.info("Users found with findAll():");
            for (User user : userDao.findAll()) {
                logger.info(user.toString());
            }
            logger.info("");

            // fetch specific user by id 1
            User userById = userDao.findOne(1L);
            logger.info("User found with findOne(1L):");
            logger.info("--------------------------------");
            logger.info(userById.toString());
            logger.info("");

            // fetch specific user by username
            User userByUsername = userDao.findByUsername("autoUsername");
            logger.info("User found with findByUsername('autoUsername')");
            logger.info("--------------------------------");
            logger.info(userByUsername.toString());
            logger.info("");

            // fetch specific user by email
            User userByEmail = userDao.findByEmail("autoEmail");
            logger.info("User found with findByEmail('autoEmail')");
            logger.info("--------------------------------");
            logger.info(userByEmail.toString());
            logger.info("");

        };
    }
}

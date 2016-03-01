package com.surrey.com3014.group5.config;

import com.surrey.com3014.group5.models.User;
import com.surrey.com3014.group5.services.IUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by spyro on 23-Feb-16.
 */
@Component
public class AppListener implements ApplicationListener{

    private static final Logger LOGGER = Logger.getLogger(AppListener.class);

    private IUserService userService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        LOGGER.trace("onApplicationEvent: " + applicationEvent.getClass().getSimpleName());

        if(applicationEvent instanceof ContextRefreshedEvent){
            LOGGER.debug("onApplicationEvent STARTING: " + applicationEvent.getClass().getSimpleName());

            // save a User
            User autoUser = new User("autoUsername", "autoPassword", "autoEmail", "autoName");
            getUserService().create(autoUser);



            LOGGER.info("Users found with findAll():");
            for (Object o : getUserService().findAll()) {
                User user = (User) o;
                LOGGER.info(user.toString());
            }
            LOGGER.info("");

            // fetch specific user by id 1
            User userById = getUserService().findOne(1L);
            LOGGER.info("User found with findOne(1L):");
            LOGGER.info("--------------------------------");
            LOGGER.info(userById.toString());
            LOGGER.info("");

            // fetch specific user by username
            User userByUsername = getUserService().findByUsername("autoUsername");
            LOGGER.info("User found with findByUsername('autoUsername')");
            LOGGER.info("--------------------------------");
            LOGGER.info(userByUsername.toString());
            LOGGER.info("");

            // fetch specific user by email
            User userByEmail = getUserService().findByEmail("autoEmail");
            LOGGER.info("User found with findByEmail('autoEmail')");
            LOGGER.info("--------------------------------");
            LOGGER.info(userByEmail.toString());
            LOGGER.info("");

            //User Validation
            LOGGER.info("User Validated: " + getUserService().validate(new User("autoUsername", "autoPassword", "autoEmail", "autoName")));

        }
    }

    public IUserService getUserService() {
        return userService;
    }


    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}

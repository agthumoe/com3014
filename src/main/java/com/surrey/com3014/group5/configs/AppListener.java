package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Aung Thu Moe
 */
@Component
public class AppListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppListener.class);

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("adminAuthority")
    private Authority adminAuthority;

    @Autowired
    @Qualifier("userAuthority")
    private Authority userAuthority;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextRefreshedEvent){
            LOGGER.debug("onApplicationEvent STARTING: " + applicationEvent.getClass().getSimpleName());

            User admin = new User("admin", "password", "admin@localhost.com", "Administrator", true);
            this.userService.create(admin);
            LOGGER.debug("onApplicationEvent() users Admin created");
            admin.addAuthority(userAuthority);
            admin.addAuthority(adminAuthority);
            this.userService.update(admin);

            LOGGER.debug("onApplicationEvent() users Admin updated");

            User user = new User("user", "password", "users@localhost.com", "Generic User", false);
            this.userService.create(user);
            LOGGER.debug("onApplicationEvent() User created");
            user.addAuthority(userAuthority);
            this.userService.update(user);
            LOGGER.debug("onApplicationEvent() User updated");
        }
    }
}


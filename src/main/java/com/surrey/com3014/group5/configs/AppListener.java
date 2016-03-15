package com.surrey.com3014.group5.configs;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static com.surrey.com3014.group5.configs.SecurityConfig.*;
/**
 * @author Aung Thu Moe
 */
@Component
public class AppListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppListener.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextRefreshedEvent){
            LOGGER.debug("onApplicationEvent STARTING: " + applicationEvent.getClass().getSimpleName());

            Authority adminAuth = new Authority();
            adminAuth.setType(ADMIN);
            this.authorityService.create(adminAuth);
            LOGGER.debug("onApplicationEvent() Admin Authority created");

            Authority userAuth = new Authority();
            userAuth.setType(USER);
            this.authorityService.create(userAuth);
            LOGGER.debug("onApplicationEvent() User Authority created");

            User admin = new User("admin", "password", "admin@localhost", "admin");
            this.userService.create(admin);
            LOGGER.debug("onApplicationEvent() user Admin created");
            admin.addAuthority(userAuth);
            admin.addAuthority(adminAuth);
            this.userService.update(admin);

            LOGGER.debug("onApplicationEvent() user Admin updated");

            User user = new User("user", "password", "user@localhost", "user");
            this.userService.create(user);
            LOGGER.debug("onApplicationEvent() User created");
            user.addAuthority(userAuth);
            this.userService.update(user);
            LOGGER.debug("onApplicationEvent() User updated");
        }
    }
}


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
            setupUser("admin", "password", "admin@localhost.com", "Administrator", true, true);
            setupUser("user", "password", "users@localhost.com", "Generic User", true, false);
            setupUser("alice", "password", "alice@localhost.com", "Alice", false, true);
            setupUser("bob", "password", "bob@localhost.com", "Bob", false, false);
            setupUser("chris", "password", "chris@localhost.com", "Chris", true, false);
            setupUser("daniel", "password", "daniel@localhost.com", "Daniel", true, true);
            setupUser("eric", "password", "eric@localhost.com", "Eric", true, false);
        }
    }

    private void setupUser(String username, String password, String email, String name, boolean enable, boolean isAdmin) {
        User user = new User(username, password, email, name, enable);
        user.addAuthority(userAuthority);
        if (isAdmin) {
            user.addAuthority(adminAuthority);
        }
        this.userService.create(user);
    }
}


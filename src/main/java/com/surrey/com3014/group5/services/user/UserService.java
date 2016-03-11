package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.MutableService;

/**
 * @author Spyros Balkonis
 */
public interface UserService extends MutableService<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean validate(User user);

}

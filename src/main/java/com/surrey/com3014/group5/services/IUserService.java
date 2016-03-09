package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.impl.User;

/**
 * Created by spyro on 23-Feb-16.
 */
public interface IUserService extends IMutableService<User>{

    User findByUsername(String username);

    User findByEmail(String email);

    boolean validate(User user);

}

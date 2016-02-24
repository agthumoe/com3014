package com.surrey.com3014.group5.services.impl;

import com.surrey.com3014.group5.daos.UserDao;
import com.surrey.com3014.group5.models.User;
import com.surrey.com3014.group5.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by spyro on 23-Feb-16.
 */
@Service("userService")
public class UserService extends MutableService<User> implements IUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao){
        super(userDao);
    }

    @Override
    public User findByUsername(String username) {
        return this.getUserDao().findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return this.getUserDao().findByEmail(email);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}

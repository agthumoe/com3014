package com.surrey.com3014.group5.services.impl;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Created by spyro on 23-Feb-16.
 */
@Service("userService")
public class UserService extends MutableService<User> implements IUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        super(userRepository);
    }

    @Override
    public User findByUsername(String username) {
        return this.getUserRepository().findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return this.getUserRepository().findByEmail(email);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public <S extends User> S create(S s) {
    	if (!(null == s.getName() || s.getName().trim().equals(""))) {
    		s.setPassword(BCrypt.hashpw(s.getPassword(), BCrypt.gensalt())); //Password hashed and salt added.
    	}
        return super.create(s);
    }

    @Override
    public boolean validate(User user){
        User userFromDatabase = this.findByEmail(user.getEmail());
        LOGGER.info("user password: " + user.getPassword());
        LOGGER.info("userFromDatabase password: " + userFromDatabase.getPassword());
        return BCrypt.checkpw(user.getPassword(), userFromDatabase.getPassword());
    }

}

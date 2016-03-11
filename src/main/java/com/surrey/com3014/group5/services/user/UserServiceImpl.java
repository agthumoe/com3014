package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.services.AbstractMutableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * @author Spyros Balkonis
 */
@Service("userService")
public class UserServiceImpl extends AbstractMutableService<User> implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
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

package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.AbstractMutableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
@Service("userService")
public class UserServiceImpl extends AbstractMutableService<User> implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return getUserRepository().findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return getUserRepository().findByEmail(email);
    }

    public UserRepository getUserRepository() {
        return (UserRepository) super.getRepository();
    }

    @Override
    public <S extends User> S create(S s) {
    	if (!(null == s.getName() || s.getName().trim().equals(""))) {
    		s.setPassword(passwordEncoder.encode(s.getPassword())); //Password hashed and salt added.
    	}
        return super.create(s);
    }

    @Override
    public boolean validate(User user) {
        Optional<User> userFromDatabase = findByEmail(user.getEmail());
        if (userFromDatabase.isPresent()) {
            return passwordEncoder.matches(user.getPassword(), userFromDatabase.get().getPassword());
        }
        throw new NotFoundException("The requested user does not exist");
    }

    @Override
    @Transactional
    public UsernamePasswordAuthenticationToken authenticate(final String username, final String password) {
        Optional<User> user = this.findByUsername(username);
        if (!user.isPresent()) {
            throw new NotFoundException("The requested user with username: " + username + " does not exist");
        }
        if (this.passwordEncoder.matches(password, user.get().getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, user.get().getAuthorities());
        } else {
            throw new BadCredentialsException("User authentication failed");
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        Optional<User> userOptional = getUserRepository().findByUsername(SecurityUtils.getCurrentLogin());
        if (userOptional.isPresent()) {
            return userOptional;
        }
        throw new NotFoundException("User has not login yet");
    }

}

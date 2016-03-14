package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.services.MutableService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Spyros Balkonis
 */
public interface UserService extends MutableService<User> {

    User create(UserDTO userDTO);

    User createUserWithAuthorities(UserDTO userDTO);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean validate(User user);

    UserRepository getUserRepository();

    UsernamePasswordAuthenticationToken authenticate(final String username, final String password);

    User getUserWithAuthorities();
}

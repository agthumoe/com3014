package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.services.MutableService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
public interface UserService extends MutableService<User> {

    User create(UserDTO userDTO);

    User createUserWithAuthorities(UserDTO userDTO);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean validate(User user);

    UserRepository getUserRepository();

    UsernamePasswordAuthenticationToken authenticate(final String username, final String password);

    Optional<User> getCurrentLogin();

    List<User> getAll();
}

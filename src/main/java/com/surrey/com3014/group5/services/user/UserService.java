package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.services.MutableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
public interface UserService extends MutableService<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByUsernameContaining(String username);

    List<User> findByEmailContaining(String email);

    List<User> findByNameContaining(String name);

    List<User> findByEnabled(boolean enabled);

    boolean validate(long id, String password);

    boolean validate(String password);

    UserRepository getUserRepository();

    UsernamePasswordAuthenticationToken authenticate(final String username, final String password);

    Optional<User> getCurrentLogin();

    List<User> getAll();

    Page<User> getPagedList(Pageable pageable);

    void updatePassword(User user);

}

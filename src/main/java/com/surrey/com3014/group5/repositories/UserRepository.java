package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.User;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to access persistence {@link User} entity.
 *
 * @author Spyros Balkonis
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User> {

    /**
     * Retrieves a {@link User} by its username.
     *
     * @param username to be searched
     * @return The {@link User} with the given username wrapped in the {@link Optional}
     * or an empty {@link Optional} container if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a {@link User} by its email.
     *
     * @param email to be searched
     * @return The {@link User} with the given email wrapped in the {@link Optional}
     * or an empty {@link Optional} container if not found.
     */
    Optional<User> findByEmail(String email);

    List<User> findByUsernameContaining(String username);

    List<User> findByEmailContaining(String email);

    List<User> findByNameContaining(String name);

    List<User> findByEnabled(boolean enabled);
}

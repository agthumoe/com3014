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

    /**
     * Find by username containing the query provided.
     *
     * @param query to be searched by
     * @return List of user satisfying the query.
     */
    List<User> findByUsernameContaining(String query);

    /**
     * Find by email containing the query provided.
     *
     * @param query to be searched by
     * @return List of user satisfying the query.
     */
    List<User> findByEmailContaining(String query);

    /**
     * find by name containing the query provided.
     *
     * @param query to be searched by
     * @return List of user satisfying the query.
     */
    List<User> findByNameContaining(String query);

    /**
     * Find by enabled (user status)
     *
     * @param enabled user status
     * @return List of user of the provided user status.
     */
    List<User> findByEnabled(boolean enabled);
}

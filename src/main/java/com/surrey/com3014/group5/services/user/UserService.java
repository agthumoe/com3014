package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.MutableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing users.
 *
 * @author Spyros Balkonis
 */
public interface UserService extends MutableService<User> {

    /**
     * Find user by username.
     *
     * @param username of a user.
     * @return user wrapped in Optional if found, empty Optional otherwise.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email of a user.
     * @return user wrapped in Optional if found, empty Optional otherwise.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find by username containing the provided query.
     *
     * @param query the requested query.
     * @return List of users satisfying the requested query.
     */
    List<User> findByUsernameContaining(String query);

    /**
     * Find by email containing the provided query.
     *
     * @param query the requested query.
     * @return List of users satisfying the requested query.
     */
    List<User> findByEmailContaining(String query);

    /**
     * Find by name containing the provided query.
     *
     * @param query the requested query.
     * @return List of users satisfying the requested query.
     */
    List<User> findByNameContaining(String query);

    /**
     * Find by user status.
     *
     * @param enabled user status
     * @return List of users satisfying the requested query.
     */
    List<User> findByEnabled(boolean enabled);

    /**
     * Validate the provided password against the hashed password of the current login user stored in the database.
     *
     * @param password to be validated
     * @return true if the password is valid.
     */
    boolean validate(String password);

    /**
     * This method validate the login username and password.
     *
     * @param username of the current login user.
     * @param password of the current login user.
     * @return UsernamePasswordAuthenticationToken
     * @see UsernamePasswordAuthenticationToken
     */
    UsernamePasswordAuthenticationToken authenticate(final String username, final String password);

    /**
     * Get current login user.
     *
     * @return current login user wrapped in the Optional.
     */
    Optional<User> getCurrentLogin();

    /**
     * Get all users.
     *
     * @return List of all users.
     */
    List<User> getAll();

    /**
     * Get pagedList satisfying the pageRequest provided.
     *
     * @param pageable the pageRequest provided.
     * @return List of users wrapped in the Page container for pagination support.
     */
    Page<User> getPagedList(Pageable pageable);

    /**
     * Update user's password.
     *
     * @param user with updated password information.
     */
    void updatePassword(User user);

}

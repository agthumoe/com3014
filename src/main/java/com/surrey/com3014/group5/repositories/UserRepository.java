package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.User;

import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}

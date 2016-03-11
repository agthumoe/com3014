package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.User;

/**
 * @author Spyros Balkonis
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User> {

    User findByUsername(String username);

    User findByEmail(String email);

}

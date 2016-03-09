package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.User;

/**
 * Created by spyro on 21-Feb-16.
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User> {

    User findByUsername(String username);

    User findByEmail(String email);

}

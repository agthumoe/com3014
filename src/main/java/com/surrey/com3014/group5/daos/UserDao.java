package com.surrey.com3014.group5.daos;

import com.surrey.com3014.group5.models.User;
import org.springframework.stereotype.Repository;

/**
 * Created by spyro on 21-Feb-16.
 */
@Repository
public interface UserDao extends IDao<User>{

    User findByUsername(String username);

    User findByEmail(String email);

}

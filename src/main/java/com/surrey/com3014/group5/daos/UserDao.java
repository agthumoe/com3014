package com.surrey.com3014.group5.daos;

import com.surrey.com3014.group5.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by spyro on 21-Feb-16.
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long>{

    User findByUsername(String username);

    User findByEmail(String email);

}

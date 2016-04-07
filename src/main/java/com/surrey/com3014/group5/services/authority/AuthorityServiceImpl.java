package com.surrey.com3014.group5.services.authority;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.repositories.AuthorityRepository;
import com.surrey.com3014.group5.repositories.Repository;
import com.surrey.com3014.group5.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.surrey.com3014.group5.security.AuthoritiesConstants.ADMIN;
import static com.surrey.com3014.group5.security.AuthoritiesConstants.USER;

/**
 * @author Aung Thu Moe
 */
@Service
public class AuthorityServiceImpl extends AbstractService<Authority> implements AuthorityService {

    @Autowired
    public AuthorityServiceImpl(Repository<Authority> repository) {
        super(repository);
    }

    @Override
    public AuthorityRepository getAuthorityRepository() {
        return (AuthorityRepository) super.getRepository();
    }

    @Override
    public Authority getAdmin() {
        Optional<Authority> maybe = getAuthorityRepository().findByType(ADMIN);
        if (maybe.isPresent()) {
            return maybe.get();
        }
        throw new IllegalArgumentException("datasource is not properly setup");
    }

    @Override
    public Authority getUser() {
        Optional<Authority> maybe = getAuthorityRepository().findByType(USER);
        if (maybe.isPresent()) {
            return maybe.get();
        }
        throw new IllegalArgumentException("datasource is not properly setup");
    }
}

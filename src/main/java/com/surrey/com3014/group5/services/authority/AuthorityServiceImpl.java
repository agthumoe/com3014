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
 * Implementation of AuthorityService.
 *
 * @author Aung Thu Moe
 * @see AuthorityService
 */
@Service
public class AuthorityServiceImpl extends AbstractService<Authority> implements AuthorityService {

    /**
     * Create a new AuthorityService with the AuthorityRepository
     *
     * @param repository AuthorityRepository to access authority data from persistent storage.
     */
    @Autowired
    public AuthorityServiceImpl(Repository<Authority> repository) {
        super(repository);
    }

    /**
     * Get AuthorityRepository
     *
     * @return AuthorityRepository
     */
    private AuthorityRepository getAuthorityRepository() {
        return (AuthorityRepository) super.getRepository();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authority getAdmin() {
        Optional<Authority> maybe = getAuthorityRepository().findByType(ADMIN);
        if (maybe.isPresent()) {
            return maybe.get();
        }
        throw new IllegalArgumentException("datasource is not properly setup");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authority getUser() {
        Optional<Authority> maybe = getAuthorityRepository().findByType(USER);
        if (maybe.isPresent()) {
            return maybe.get();
        }
        throw new IllegalArgumentException("datasource is not properly setup");
    }
}

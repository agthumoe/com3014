package com.surrey.com3014.group5.services.authority;

import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.repositories.AuthorityRepository;
import com.surrey.com3014.group5.repositories.Repository;
import com.surrey.com3014.group5.services.AbstractImmutableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
@Service
public class AuthorityServiceImpl extends AbstractImmutableService<Authority> implements AuthorityService {

    @Autowired
    public AuthorityServiceImpl(Repository<Authority> repository) {
        super(repository);
    }

    @Override
    public Optional<Authority> findByType(String type) {
        Optional<Authority> authorities = getAuthorityRepository().findByType(type);
        if (authorities.isPresent()) {
            return authorities;
        }
        throw new NotFoundException("Authority with type: " + type + " does not exist");
    }

    @Override
    public AuthorityRepository getAuthorityRepository() {
        return (AuthorityRepository) super.getRepository();
    }
}

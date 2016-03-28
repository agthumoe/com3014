package com.surrey.com3014.group5.services.authority;

import com.surrey.com3014.group5.exceptions.HttpStatusException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.repositories.AuthorityRepository;
import com.surrey.com3014.group5.repositories.Repository;
import com.surrey.com3014.group5.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<Authority> findByType(String type) {
        Optional<Authority> authorities = getAuthorityRepository().findByType(type);
        if (authorities.isPresent()) {
            return authorities;
        }
        throw new HttpStatusException("Authority with type: " + type + " does not exist");
    }

    @Override
    public AuthorityRepository getAuthorityRepository() {
        return (AuthorityRepository) super.getRepository();
    }
}

package com.surrey.com3014.group5.services.authority;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.repositories.AuthorityRepository;
import com.surrey.com3014.group5.services.ImmutableService;

import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
public interface AuthorityService extends ImmutableService<Authority> {
    Optional<Authority> findByType(String type);
    AuthorityRepository getAuthorityRepository();
}

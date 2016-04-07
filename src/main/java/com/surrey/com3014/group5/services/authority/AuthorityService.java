package com.surrey.com3014.group5.services.authority;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.repositories.AuthorityRepository;
import com.surrey.com3014.group5.services.Service;

/**
 * @author Aung Thu Moe
 */
public interface AuthorityService extends Service<Authority> {
    AuthorityRepository getAuthorityRepository();
    Authority getAdmin();
    Authority getUser();
}

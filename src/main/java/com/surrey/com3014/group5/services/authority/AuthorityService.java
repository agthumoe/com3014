package com.surrey.com3014.group5.services.authority;

import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.services.Service;

/**
 * Service to access user authority.
 *
 * @author Aung Thu Moe
 */
public interface AuthorityService extends Service<Authority> {

    /**
     * Get admin authority.
     *
     * @return admin authority.
     */
    Authority getAdmin();

    /**
     * Get user authority.
     *
     * @return user authority.
     */
    Authority getUser();
}

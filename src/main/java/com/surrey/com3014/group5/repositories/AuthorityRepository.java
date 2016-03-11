package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.Authority;

import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
public interface AuthorityRepository extends Repository<Authority> {
    Optional<Authority> findByType(String type);
}

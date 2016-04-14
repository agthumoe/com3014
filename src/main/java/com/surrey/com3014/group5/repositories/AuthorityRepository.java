package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.Authority;

import java.util.Optional;

/**
 * JPA repository to acess persistence Authority entity.
 *
 * @author Aung Thu Moe
 */
public interface AuthorityRepository extends Repository<Authority> {
    /**
     * Retrieves an {@link Authority} by its type.
     *
     * @param type to be searched
     * @return The {@link Authority} with the given type wrapped in the {@link Optional}
     * or an empty {@link Optional} container if not found.
     */
    Optional<Authority> findByType(String type);
}

package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.Entity;

import java.util.Optional;

/**
 * Simple service interface.
 *
 * @author Spyros Balkonis
 */
public interface Service<T extends Entity> {

    /**
     * Create a new entity.
     *
     * @param s an entity.
     * @return created entity
     */
    <S extends T> S create(S s);

    /**
     * Find all
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Find specific one with the provided id
     *
     * @param id the provided id
     * @return the entity of the provided id
     */
    Optional<T> findOne(long id);

    /**
     * Check if the entity of the provided id exist.
     *
     * @param id the provided id
     * @return true if exist, false otherwise.
     */
    boolean exists(long id);

    /**
     * Count the total number of elements
     *
     * @return total number of elements.
     */
    long count();

    /**
     * Delete the specific entity of the provided id
     *
     * @param id the provided id
     */
    void delete(long id);

    /**
     * Delete the specific entity provided.
     *
     * @param t the specific entity
     */
    void delete(T t);
}

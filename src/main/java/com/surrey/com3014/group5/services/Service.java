package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.Entity;

import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
public interface Service<T extends Entity> {

    <S extends T> S create(S s);

    Iterable<T> findAll();

    Optional<T> findOne(long id);

    boolean exists(long id);

    long count();

    void delete(long id);

    void delete(T t);
}

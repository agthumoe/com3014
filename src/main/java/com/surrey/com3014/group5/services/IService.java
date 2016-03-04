package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.AnEntity;

/**
 * Created by spyro on 23-Feb-16.
 */
public interface IService<T extends AnEntity> {

    <S extends T> S create(S s);

    Iterable<T> findAll();

    T findOne(long id);

    boolean exists(long id);

    long count();

    void delete(long id);

    void delete(T t);
}

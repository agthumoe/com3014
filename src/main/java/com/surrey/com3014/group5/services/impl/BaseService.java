package com.surrey.com3014.group5.services.impl;

import com.surrey.com3014.group5.models.AnEntity;
import com.surrey.com3014.group5.repositories.Repository;
import com.surrey.com3014.group5.services.IService;

import javax.persistence.EntityNotFoundException;

/**
 * Created by spyro on 23-Feb-16.
 */
public abstract class BaseService <T extends AnEntity> implements IService<T>{

    private final Repository<T> repository;

    public BaseService(Repository<T> repository){
        this.repository = repository;
    }

    @Override
    public <S extends T> S create(S s) {
        return (S) this.getRepository().save(s);
    }

    @Override
    public Iterable<T> findAll() {
        return this.getRepository().findAll();
    }

    @Override
    public T findOne(long id) {
        if(this.exists(id)) {
            return (T) this.getRepository().findOne(id);
        }else {
            throw new EntityNotFoundException("Entity with id: " + id + " not found");
        }
    }

    @Override
    public boolean exists(long id) {
        return this.getRepository().exists(id);
    }

    @Override
    public long count() {
        return this.getRepository().count();
    }

    @Override
    public void delete(long id) {
        this.getRepository().delete(id);
    }

    @Override
    public void delete(T t) {
        this.getRepository().delete(t);
    }

    public Repository<T> getRepository() {
        return repository;
    }
}

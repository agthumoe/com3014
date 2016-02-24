package com.surrey.com3014.group5.services.impl;

import com.surrey.com3014.group5.daos.IDao;
import com.surrey.com3014.group5.models.AnEntity;
import com.surrey.com3014.group5.services.IService;

import javax.persistence.EntityNotFoundException;

/**
 * Created by spyro on 23-Feb-16.
 */
public abstract class BaseService <T extends AnEntity> implements IService<T>{

    private final IDao dao;

    public BaseService(IDao dao){
        this.dao = dao;
    }

    @Override
    public <S extends T> S create(S s) {
        return (S) this.getDao().save(s);
    }

    @Override
    public Iterable findAll() {
        return this.getDao().findAll();
    }

    @Override
    public T findOne(long id) {
        if(this.exists(id)) {
            return (T) this.getDao().findOne(id);
        }else {
            throw new EntityNotFoundException("Entity with id: " + id + " not found");
        }
    }

    @Override
    public boolean exists(long id) {
        return this.getDao().exists(id);
    }

    @Override
    public long count() {
        return this.getDao().count();
    }

    @Override
    public void delete(long id) {
        this.getDao().delete(id);
    }

    @Override
    public void delete(T t) {
        this.getDao().delete(t);
    }

    public IDao getDao() {
        return dao;
    }
}

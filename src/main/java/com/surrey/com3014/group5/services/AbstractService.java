package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.Entity;
import com.surrey.com3014.group5.repositories.Repository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
public abstract class AbstractService<T extends Entity> implements Service<T> {

    private final Repository<T> repository;

    public AbstractService(Repository<T> repository){
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
    public Optional<T> findOne(long id) {
        return Optional.ofNullable(getRepository().findOne(id));
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

package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.Entity;
import com.surrey.com3014.group5.repositories.Repository;

import java.util.Optional;

/**
 * Basic abstract service contains CRUD functionalities to handle persistent data.
 *
 * @author Spyros Balkonis
 */
public abstract class AbstractService<T extends Entity> implements Service<T> {
    /**
     * A repository to access persistent data storage.
     */
    private final Repository<T> repository;

    /**
     * Create a new service with the provided repository
     *
     * @param repository to access persistent data storage.
     */
    public AbstractService(Repository<T> repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends T> S create(S s) {
        return this.getRepository().save(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<T> findAll() {
        return this.getRepository().findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> findOne(long id) {
        return Optional.ofNullable(getRepository().findOne(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(long id) {
        return this.getRepository().exists(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        return this.getRepository().count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(long id) {
        this.getRepository().delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T t) {
        this.getRepository().delete(t);
    }

    /**
     * {@inheritDoc}
     */
    public Repository<T> getRepository() {
        return repository;
    }
}

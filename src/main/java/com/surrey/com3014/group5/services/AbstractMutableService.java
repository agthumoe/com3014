package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.MutableModel;
import com.surrey.com3014.group5.repositories.Repository;

import java.util.Date;

/**
 * Abstract mutable service has method to update the model.
 *
 * @author Spyros Balkonis
 */
public class AbstractMutableService<T extends MutableModel> extends AbstractImmutableService<T> implements MutableService<T> {

    /**
     * Create a new service with the provided repository
     *
     * @param repository to access persistent data storage.
     */
    public AbstractMutableService(Repository<T> repository) {
        super(repository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends T> S create(S s) {
        s.setLastModifiedDate(new Date());
        return super.create(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends T> Iterable<S> create(Iterable<S> s) {
        for (S as : s) {
            as.setLastModifiedDate(new Date());
        }
        return super.create(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends T> S update(S s) {
        s.setLastModifiedDate(new Date());
        return this.getRepository().save(s);
    }
}

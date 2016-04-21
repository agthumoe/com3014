package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.DateStampedModel;
import com.surrey.com3014.group5.repositories.Repository;

/**
 * Abstract immutable service can create a new model.
 *
 * @author Spyros Balkonis
 */
public abstract class AbstractImmutableService<T extends DateStampedModel> extends AbstractService<T> implements ImmutableService<T> {

    /**
     * Create a new service with the provided repository.
     *
     * @param repository to access persistent data storage.
     */
    public AbstractImmutableService(Repository<T> repository) {
        super(repository);
    }

    @Override
    public <S extends T> Iterable<S> create(Iterable<S> s) {
        return this.getRepository().save(s);
    }
}

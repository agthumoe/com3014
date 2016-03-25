package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.DateStampedModel;
import com.surrey.com3014.group5.repositories.Repository;

import java.util.Date;

/**
 * @author Spyros Balkonis
 */
public abstract class AbstractImmutableService<T extends DateStampedModel> extends AbstractService<T> implements ImmutableService<T> {

    public AbstractImmutableService(Repository<T> repository){
        super(repository);
    }

    @Override
    public <S extends T> S create(S s) {
        return this.getRepository().save(s);
    }

    @Override
    public <S extends T> Iterable<S> create(Iterable<S> s) {
        return this.getRepository().save(s);
    }
}

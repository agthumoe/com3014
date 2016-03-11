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
        s.setCreatedDate(new Date());
        return (S) this.getRepository().save(s);
    }

    @Override
    public <S extends T> Iterable<S> create(Iterable<S> s) {
        for(S as : s){
            as.setCreatedDate(new Date());
        }
        return this.getRepository().save(s);
    }
}

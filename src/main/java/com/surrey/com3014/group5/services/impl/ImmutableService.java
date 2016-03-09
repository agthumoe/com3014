package com.surrey.com3014.group5.services.impl;

import com.surrey.com3014.group5.models.ADateStampedModel;
import com.surrey.com3014.group5.repositories.Repository;
import com.surrey.com3014.group5.services.IImmutableService;

import java.util.Date;

/**
 * Created by spyro on 23-Feb-16.
 */
public abstract class ImmutableService <T extends ADateStampedModel> extends BaseService<T> implements IImmutableService<T> {

    public ImmutableService(Repository<T> repository){
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

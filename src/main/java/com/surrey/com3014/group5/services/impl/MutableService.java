package com.surrey.com3014.group5.services.impl;

import com.surrey.com3014.group5.models.AMutableModel;
import com.surrey.com3014.group5.repositories.Repository;
import com.surrey.com3014.group5.services.IMutableService;

import java.util.Date;

/**
 * Created by spyro on 23-Feb-16.
 */
public class MutableService<T extends AMutableModel> extends ImmutableService<T> implements IMutableService<T> {

    public MutableService(Repository<T> repository){
        super(repository);
    }

    @Override
    public <S extends T> S create(S s) {
        s.setLastModified(new Date());
        return super.create(s);
    }

    @Override
    public <S extends T> Iterable<S> create(Iterable<S> s) {
        for(S as : s){
            as.setLastModified(new Date());
        }
        return super.create(s);
    }

    @Override
    public <S extends T> S update(S s) {
        s.setLastModified(new Date());
        return (S) this.getRepository().save(s);
    }
}

package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.MutableModel;
import com.surrey.com3014.group5.repositories.Repository;

import java.util.Date;

/**
 * @author Spyros Balkonis
 */
public class AbstractMutableService<T extends MutableModel> extends AbstractImmutableService<T> implements MutableService<T> {

    public AbstractMutableService(Repository<T> repository){
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
        return this.getRepository().save(s);
    }
}

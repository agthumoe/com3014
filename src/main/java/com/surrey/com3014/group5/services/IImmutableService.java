package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.ADateStampedModel;

/**
 * Created by spyro on 23-Feb-16.
 */
public interface IImmutableService <T extends ADateStampedModel> extends IService<T>{

    @Override
    <S extends T> S create(S s);

    <S extends T> Iterable<S> create(Iterable<S> s);
}

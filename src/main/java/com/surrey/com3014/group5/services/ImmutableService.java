package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.DateStampedModel;

/**
 * @author Spyros Balkonis
 */
public interface ImmutableService<T extends DateStampedModel> extends Service<T> {

    @Override
    <S extends T> S create(S s);

    <S extends T> Iterable<S> create(Iterable<S> s);
}

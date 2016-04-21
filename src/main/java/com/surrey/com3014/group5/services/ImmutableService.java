package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.DateStampedModel;

/**
 * A service to handle an immutable model.
 *
 * @author Spyros Balkonis
 */
public interface ImmutableService<T extends DateStampedModel> extends Service<T> {
    /**
     * Create iteratable list of entities.
     *
     * @param s iteratable list of entities
     * @return created iteratable list of entities
     */
    <S extends T> Iterable<S> create(Iterable<S> s);
}

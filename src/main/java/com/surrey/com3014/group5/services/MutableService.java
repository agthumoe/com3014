package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.MutableModel;

/**
 * @author Spyros Balkonis
 */
public interface MutableService<T extends MutableModel> extends ImmutableService<T> {

    <S extends T> S update(S s);
}

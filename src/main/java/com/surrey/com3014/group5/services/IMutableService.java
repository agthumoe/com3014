package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.AMutableModel;

/**
 * Created by spyro on 23-Feb-16.
 */
public interface IMutableService<T extends AMutableModel> extends IImmutableService<T>{

    <S extends T> S update(S s);
}

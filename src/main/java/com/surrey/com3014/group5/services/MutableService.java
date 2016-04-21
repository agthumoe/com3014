package com.surrey.com3014.group5.services;

import com.surrey.com3014.group5.models.MutableModel;

/**
 * A service to handle a mutable model.
 *
 * @author Spyros Balkonis
 */
public interface MutableService<T extends MutableModel> extends ImmutableService<T> {

    /**
     * Method to update the model.
     *
     * @param s mutable model to be updated.
     * @return the updated mutable model.
     */
    <S extends T> S update(S s);
}

package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.AnEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by spyro on 23-Feb-16.
 */
@NoRepositoryBean
public interface Repository<T extends AnEntity> extends CrudRepository<T, Long> {
}

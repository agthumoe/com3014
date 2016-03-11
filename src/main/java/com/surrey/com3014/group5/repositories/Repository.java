package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Spyros Balkonis
 */
@NoRepositoryBean
public interface Repository<T extends Entity> extends JpaRepository<T, Long> {
}

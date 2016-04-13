package com.surrey.com3014.group5.models;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Entity object maps a record in the database table.
 *
 * @author Spyros Balkonis
 */
@MappedSuperclass
public abstract class Entity implements Serializable {
    private static final long serialVersionUID = -158342729603651283L;
}

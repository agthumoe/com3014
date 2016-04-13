package com.surrey.com3014.group5.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Classes which extends this model will have and auto increment id as a primary key.
 *
 * @author Spyros Balkonis
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Model extends Entity {

    private static final long serialVersionUID = 3289023988626537358L;

    /**
     * Auto increment id as a primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    /**
     * Getter for id
     *
     * @return id of this record.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id of this record.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        return getId() == model.getId();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}

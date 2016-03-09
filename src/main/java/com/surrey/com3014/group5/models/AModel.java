package com.surrey.com3014.group5.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Created by spyro on 23-Feb-16.
 */
@MappedSuperclass
public abstract class AModel extends AnEntity{

	private static final long serialVersionUID = 3289023988626537358L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AModel aModel = (AModel) o;

        return getId() == aModel.getId();

    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}

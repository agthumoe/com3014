package com.surrey.com3014.group5.models;

import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Spyros Balkonis
 *
 * A model that can change over time
 */
@MappedSuperclass
public abstract class MutableModel extends DateStampedModel {

	private static final long serialVersionUID = 2617383166882423052L;
	@Column
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModified;

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}

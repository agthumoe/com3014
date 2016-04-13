package com.surrey.com3014.group5.models;

import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * A model that can be updated over time.</br>
 * Classes which extend this class will have lastModifiedDate attribute.
 *
 * @author Spyros Balkonis
 */
@MappedSuperclass
public abstract class MutableModel extends DateStampedModel {

    private static final long serialVersionUID = 2617383166882423052L;

    /**
     * Last modified date of this record.
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;

    /**
     * Get the last modified date of this record.
     *
     * @return lastModifiedDate of this record.
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Set the last modified date of this record.
     *
     * @param lastModifiedDate of this record.
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

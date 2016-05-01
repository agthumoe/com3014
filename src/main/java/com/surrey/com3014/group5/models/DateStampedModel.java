package com.surrey.com3014.group5.models;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Classes which extend this class will have a createdDate attribute.
 *
 * @author Spyros Balkonis
 */
@MappedSuperclass
public abstract class DateStampedModel extends Model {

    private static final long serialVersionUID = 7177871077863701747L;

    /**
     * Created date of this record.
     */
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    /**
     * Default constructor initialise createdDate
     */
    public DateStampedModel() {
        super();
        this.createdDate = new Date();
    }

    /**
     * Get the createdDate
     *
     * @return createdDate of this record.
     */
    public Date getCreatedDate() {
        return createdDate;
    }

}

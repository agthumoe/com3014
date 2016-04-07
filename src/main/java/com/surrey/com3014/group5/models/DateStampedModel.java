package com.surrey.com3014.group5.models;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Spyros Balkonis
 */
@MappedSuperclass
public class DateStampedModel extends Model {

	private static final long serialVersionUID = 7177871077863701747L;
	@Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    public DateStampedModel() {
        super();
        this.created = new Date();
    }

    public Date getCreated() {
        return created;
    }
}

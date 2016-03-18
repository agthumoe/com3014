package com.surrey.com3014.group5.models.impl;

import com.surrey.com3014.group5.models.Model;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Aung Thu Moe
 */
@Entity
@Table(name = "authority")
public class Authority extends Model implements GrantedAuthority {

    private static final long serialVersionUID = -8735115732090836845L;

    public Authority() {
        super();
    }

    @Column
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getAuthority(){
        return type;
    }

}

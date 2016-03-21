package com.surrey.com3014.group5.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import io.swagger.annotations.ApiModel;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Aung Thu Moe
 */
@ApiModel("ManagedUserDTO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ManagedUserDTO extends UserDTO {
    private static final long serialVersionUID = 696087880996602691L;

    private Long id;
    private Date createdDate;
    private Date lastModifiedDate;

    public ManagedUserDTO() {
        super();
    }

    public ManagedUserDTO(User user) {
        super(user);
        this.createdDate = user.getCreatedDate();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.setAuthorities(user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

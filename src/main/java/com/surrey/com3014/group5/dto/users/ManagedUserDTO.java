package com.surrey.com3014.group5.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.surrey.com3014.group5.dto.Credentials;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.AuthoritiesConstants;
import io.swagger.annotations.ApiModel;

import java.text.SimpleDateFormat;

/**
 * @author Aung Thu Moe
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel
public class ManagedUserDTO extends UserDTO implements Credentials {
    private static final long serialVersionUID = 8867610531231596079L;
    private String createdDate;
    private String lastModifiedDate;
    private boolean enabled;
    private boolean admin;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String confirmPassword;

    public ManagedUserDTO() {
        super();
    }

    public ManagedUserDTO(User user) {
        super(user);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.createdDate = formatter.format(user.getCreatedDate());
        this.lastModifiedDate = formatter.format(user.getLastModifiedDate());
        this.enabled = user.isEnabled();
        this.password = null;
        this.confirmPassword = null;
        user.getAuthorities().stream().filter(authority -> authority.getAuthority().equals(AuthoritiesConstants.ADMIN)).forEach(authority -> this.setAdmin(true));
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String getConfirmPassword() {
        return this.confirmPassword;
    }
}

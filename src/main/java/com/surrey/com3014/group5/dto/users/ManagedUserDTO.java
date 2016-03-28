package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.Password;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.AuthoritiesConstants;
import java.util.Date;

/**
 * @author Aung Thu Moe
 */
public class ManagedUserDTO extends UserDTO implements Password {
    private static final long serialVersionUID = 8867610531231596079L;
    private Date createdDate;
    private Date lastModifiedDate;
    private boolean enabled;
    private boolean admin;
    private String password;
    private String confirmPassword;

    public ManagedUserDTO() {
        super();
    }

    public ManagedUserDTO(User user) {
        super(user);
        this.createdDate = user.getCreatedDate();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.enabled = user.isEnabled();
        this.password = null;
        this.confirmPassword = null;
        for (Authority authority: user.getAuthorities()) {
            if (authority.getAuthority().equals(AuthoritiesConstants.ADMIN)) {
                this.setAdmin(true);
            }
        }
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

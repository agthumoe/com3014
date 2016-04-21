package com.surrey.com3014.group5.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.surrey.com3014.group5.dto.Credentials;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.AuthoritiesConstants;
import io.swagger.annotations.ApiModel;

import java.text.SimpleDateFormat;

/**
 * Data transfer object for user management.
 *
 * @author Aung Thu Moe
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel
public class ManagedUserDTO extends UserDTO implements Credentials {
    private static final long serialVersionUID = 8867610531231596079L;
    /**
     * Created date of this user.
     */
    private String createdDate;
    /**
     * Last modified date of thi suser.
     */
    private String lastModifiedDate;
    /**
     * Status of this user.
     */
    private boolean enabled;
    /**
     * Role of this user.
     */
    private boolean admin;
    /**
     * Current user password.
     */
    @JsonIgnore
    private String password;
    /**
     * Confirm password.
     */
    @JsonIgnore
    private String confirmPassword;

    /**
     * Default constructor.
     */
    public ManagedUserDTO() {
        super();
    }

    /**
     * Initialise new ManagedUserDTO from user model.
     *
     * @param user User model
     */
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

    /**
     * Getter for createdDate.
     *
     * @return createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter for createdDate.
     *
     * @param createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Getter for lastModifiedDate.
     *
     * @return lastModifiedDate
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Setter for lastModifiedDate.
     *
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Getter for user account status.
     *
     * @return true if account is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Setter for user account status.
     *
     * @param enabled user account status.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Getter for user role.
     *
     * @return true if user has ADMIN role, false otherwise.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Setter for user role.
     *
     * @param admin user role.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

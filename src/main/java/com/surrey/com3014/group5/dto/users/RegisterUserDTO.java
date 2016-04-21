package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.Credentials;
import com.surrey.com3014.group5.models.impl.User;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Data transfer object for user registration.
 *
 * @author Aung Thu Moe
 */
public class RegisterUserDTO extends UserDTO implements Credentials {
    private static final long serialVersionUID = -4217792129287394735L;

    /**
     * User's password.
     */
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    /**
     * Confirm password.
     */
    private String confirmPassword;

    /**
     * Default constructor.
     */
    public RegisterUserDTO() {
        super();
    }

    /**
     * Initialise new RegisterUserDTO object from User Model.
     *
     * @param user User model.
     */
    public RegisterUserDTO(User user) {
        super(user);
        this.password = null;
        this.confirmPassword = null;
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

package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.Credentials;
import com.surrey.com3014.group5.dto.Verifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Data transfer object to handle password update.
 *
 * @author Aung Thu Moe
 */
public class UpdatePasswordDTO implements Credentials, Verifiable {
    /**
     * Current login password to validate credentials.
     */
    @NotBlank
    private String currentPassword;

    /**
     * New password to be updated.
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
    public UpdatePasswordDTO() {
        super();
    }

    /**
     * Initialise UpdatePasswordDTO from provided parameters.
     *
     * @param currentPassword current login password.
     * @param password        new password to be updated.
     * @param confirmPassword confirm password must be the same as new password.
     */
    public UpdatePasswordDTO(String currentPassword, String password, String confirmPassword) {
        super();
        this.currentPassword = currentPassword;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPassword() {
        return this.currentPassword;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return this.password;
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

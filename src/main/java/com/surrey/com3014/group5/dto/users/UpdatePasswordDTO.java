package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.Verifiable;
import com.surrey.com3014.group5.dto.Credentials;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author Aung Thu Moe
 */
public class UpdatePasswordDTO implements Credentials, Verifiable {
    @NotBlank
    private String currentPassword;
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private String confirmPassword;

    public UpdatePasswordDTO() {
        super();
    }

    public UpdatePasswordDTO(String currentPassword, String password, String confirmPassword) {
        super();
        this.currentPassword = currentPassword;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @Override
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    @Override
    public String getCurrentPassword() {
        return this.currentPassword;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
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

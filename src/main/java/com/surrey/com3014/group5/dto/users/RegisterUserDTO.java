package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.Password;
import com.surrey.com3014.group5.models.impl.User;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Aung Thu Moe
 */
public class RegisterUserDTO extends UserDTO implements Password {
    private static final long serialVersionUID = -4217792129287394735L;
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private String confirmPassword;

    public RegisterUserDTO() {
        super();
    }

    public RegisterUserDTO(User user) {
        super(user);
        this.password = null;
        this.confirmPassword = null;
    }

    public RegisterUserDTO(long id, String username, String email, String name, String password, String confirmPassword) {
        super(id, username, email, name);
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
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

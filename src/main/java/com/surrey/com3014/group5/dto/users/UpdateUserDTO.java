package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.Verifiable;
import com.surrey.com3014.group5.models.impl.User;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Aung Thu Moe
 */
public class UpdateUserDTO extends UserDTO implements Verifiable {
    private static final long serialVersionUID = -2623120141304335028L;

    @NotBlank
    private String currentPassword;

    public UpdateUserDTO() {
        super();
    }

    public UpdateUserDTO(User user) {
        super(user);
        this.currentPassword = null;
    }

    @Override
    public String getCurrentPassword() {
        return this.currentPassword;
    }

    @Override
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}

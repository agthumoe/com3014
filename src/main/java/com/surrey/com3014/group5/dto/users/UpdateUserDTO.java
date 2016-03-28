package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.dto.CurrentPassword;
import com.surrey.com3014.group5.models.impl.User;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Aung Thu Moe
 */
public class UpdateUserDTO extends UserDTO implements CurrentPassword {
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

    public UpdateUserDTO(long id, String username, String email, String name, String currentPassword) {
        super(id, username, email, name);
        this.currentPassword = currentPassword;
    }

    @Override
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    @Override
    public String getCurrentPassword() {
        return this.currentPassword;
    }
}

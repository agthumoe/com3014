package com.surrey.com3014.group5.dto;

import com.surrey.com3014.group5.security.Role;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Aung Thu Moe
 */
public class UserDTO {
    @NotEmpty
    private String email = "";

    @NotEmpty
    private String password = "";

    @NotEmpty
    private String passwordRepeated = "";

    @NotNull
    private Role role = Role.USER;
}

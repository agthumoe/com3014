package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate the provided username has already been register.
 *
 * @author Aung Thu Moe
 */
@Component("duplicateUsernameValidator")
public class DuplicateUsernameValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userService.findByUsername(userDTO.getUsername()).isPresent()) {
            errors.rejectValue("username", "Duplicate.username", "Username already registered");
        }
    }
}

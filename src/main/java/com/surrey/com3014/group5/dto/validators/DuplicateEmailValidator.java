package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate the provided email has already been registered.
 *
 * @author Aung Thu Moe
 */
@Component("duplicateEmailValidator")
public class DuplicateEmailValidator implements Validator {
    /**
     * Userservice to check if the email has already been registered.
     */
    @Autowired
    private UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "Duplicate.email", "Email already registered");
        }
    }
}

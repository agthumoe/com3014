package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Aung Thu Moe
 */
@Component("duplicateEmailValidator")
public class DuplicateEmailValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "Duplicate.email", "Email already registered");
        }
    }
}

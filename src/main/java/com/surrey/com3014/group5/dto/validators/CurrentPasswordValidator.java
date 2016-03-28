package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.CurrentPassword;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Aung Thu Moe
 */
@Component("currentPasswordValidator")
public class CurrentPasswordValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CurrentPassword.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CurrentPassword currentPassword = (CurrentPassword) target;
        if (!userService.validate(currentPassword.getCurrentPassword())) {
            errors.rejectValue("currentPassword", "Invalid.currentPassword", "Invalid Current Password");
        }
    }
}

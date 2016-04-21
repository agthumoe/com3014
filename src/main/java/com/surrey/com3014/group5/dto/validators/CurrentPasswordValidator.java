package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.Verifiable;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate current password with the stored password.
 *
 * @author Aung Thu Moe
 */
@Component("currentPasswordValidator")
public class CurrentPasswordValidator implements Validator {
    /**
     * User service to validate login user's password.
     */
    @Autowired
    private UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Verifiable.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        Verifiable verifiable = (Verifiable) target;
        if (!userService.validate(verifiable.getCurrentPassword())) {
            errors.rejectValue("currentPassword", "Invalid.currentPassword", "Invalid Current password");
        }
    }
}

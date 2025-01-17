package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.Credentials;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validate if the confirm password is the same as current password.
 *
 * @author Aung Thu Moe
 */
@Component("confirmPasswordValidator")
public class ConfirmPasswordValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials) target;
        if (!(credentials.getPassword() == null || credentials.getConfirmPassword() == null)) {
            if (!credentials.getPassword().equals(credentials.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "Diff.confirmPassword", "Confirm password is different from password");
            }
        }
    }
}

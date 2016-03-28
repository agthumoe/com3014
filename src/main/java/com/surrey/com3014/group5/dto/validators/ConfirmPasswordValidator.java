package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.Password;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Aung Thu Moe
 */
@Component("confirmPasswordValidator")
public class ConfirmPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Password.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Password password = (Password) target;
        if (! (password.getPassword() == null || password.getConfirmPassword() == null) ){
            if (!password.getPassword().equals(password.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "Diff.confirmPassword", "Confirm password is different from password");
            }
        }
    }
}

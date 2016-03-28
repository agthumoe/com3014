package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.Password;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.surrey.com3014.group5.dto.Password.PASSWORD_MAX_LENGTH;
import static com.surrey.com3014.group5.dto.Password.PASSWORD_MIN_LENGTH;

/**
 * @author Aung Thu Moe
 */
@Component("nullablePasswordSizeValidator")
public class NullablePasswordSizeValidator extends ConfirmPasswordValidator {

    @Override
    public void validate(Object target, Errors errors) {
        Password password = (Password) target;
        if ( !(password.getPassword() == null || "".equals(password.getPassword())) ) {
            if (password.getPassword().length() < PASSWORD_MIN_LENGTH || password.getPassword().length() > PASSWORD_MAX_LENGTH) {
                errors.rejectValue("password", "Size.email", "size must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH);
            }
            super.validate(target, errors);
        }
    }
}

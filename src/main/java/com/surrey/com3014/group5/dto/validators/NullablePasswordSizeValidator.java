package com.surrey.com3014.group5.dto.validators;

import com.surrey.com3014.group5.dto.Credentials;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.surrey.com3014.group5.dto.Credentials.PASSWORD_MAX_LENGTH;
import static com.surrey.com3014.group5.dto.Credentials.PASSWORD_MIN_LENGTH;

/**
 * @author Aung Thu Moe
 */
@Component("nullablePasswordSizeValidator")
public class NullablePasswordSizeValidator extends ConfirmPasswordValidator {

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials) target;
        if ( !(credentials.getPassword() == null || "".equals(credentials.getPassword())) ) {
            if (credentials.getPassword().length() < PASSWORD_MIN_LENGTH || credentials.getPassword().length() > PASSWORD_MAX_LENGTH) {
                errors.rejectValue("password", "Size.password", "size must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH);
            }
            super.validate(target, errors);
        }
    }
}

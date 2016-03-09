package com.surrey.com3014.group5.handlers;

import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.errors.ValidationErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * This class handles exception and constracts a proper error message.
 *
 * @author Aung Thu Moe
 */
@ControllerAdvice(basePackages = {"com.surrey.com3014.group5.controllers"})
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * This method handles {@link DataIntegrityViolationException}
     * @param ex DataIntegrityViolationException
     * @return error message in json format
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorDTO handleDataIntegrityConstraintViolationException(DataIntegrityViolationException ex) {
        LOGGER.debug(ex.getMessage());
        LOGGER.debug(ex.getCause().getMessage());
        return new ErrorDTO(HttpStatus.BAD_REQUEST, ex, ex.getRootCause().getMessage());

    }

    /**
     * This method handles {@link ConstraintViolationException}
     * @param ex ConstraintViolationException
     * @return error message in json format
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ValidationErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        final ValidationErrorDTO error = new ValidationErrorDTO(HttpStatus.BAD_REQUEST, ex);
        for (ConstraintViolation<?> v: ex.getConstraintViolations()) {
            error.addMessage(v.getPropertyPath().toString(), v.getMessage());
        }
        return error;
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    protected ErrorDTO handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorDTO(HttpStatus.NOT_FOUND, ex, ex.getMessage());
    }
}

package com.surrey.com3014.group5.handlers;

import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.errors.ValidationErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * This class handles exception and constructs a proper errors message for api controllers.
 *
 * @author Aung Thu Moe
 */
@ControllerAdvice(basePackages = {"com.surrey.com3014.group5.controllers.api"})
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * This method handles {@link DataIntegrityViolationException}
     *
     * @param ex DataIntegrityViolationException
     * @return errors message in json format with http status (400)
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorDTO handleDataIntegrityConstraintViolationException(DataIntegrityViolationException ex) {
        LOGGER.debug(ex.getMessage());
        LOGGER.debug(ex.getCause().getMessage());
        return new ErrorDTO(HttpStatus.BAD_REQUEST, ex.getRootCause().getMessage());

    }

    /**
     * This method handles {@link ConstraintViolationException}
     *
     * @param ex ConstraintViolationException
     * @return errors message in json format with http status (400)
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ValidationErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        final ValidationErrorDTO error = new ValidationErrorDTO(HttpStatus.BAD_REQUEST);
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            error.addMessage(v.getPropertyPath().toString(), v.getMessage());
        }
        return error;
    }


    /**
     * This method handles any exception.
     *
     * @param request HttpServletRequest information.
     * @param ex      exception to be handled
     * @return error message in json format
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseEntity<?> handleAnyException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(new ErrorDTO(status, ex.getMessage()), status);
    }

    /**
     * This method handles {@link AuthenticationException}
     *
     * @param ex {@link AuthenticationException} to be handled
     * @return error message in json format with Http status (401)
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    protected ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method get the http status out of {@link HttpServletRequest}
     *
     * @param request {@link HttpServletRequest}
     * @return {@link HttpStatus} of the provided request
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

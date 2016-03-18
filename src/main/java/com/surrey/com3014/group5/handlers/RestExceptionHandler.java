package com.surrey.com3014.group5.handlers;

import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.errors.ValidationErrorDTO;
import com.surrey.com3014.group5.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * This class handles exception and constracts a proper errors message.
 *
 * @author Aung Thu Moe
 */
@ControllerAdvice(basePackages = {"com.surrey.com3014.group5.controllers"})
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * This method handles {@link DataIntegrityViolationException}
     * @param ex DataIntegrityViolationException
     * @return errors message in json format
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
     * @param ex ConstraintViolationException
     * @return errors message in json format
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ValidationErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        final ValidationErrorDTO error = new ValidationErrorDTO(HttpStatus.BAD_REQUEST);
        for (ConstraintViolation<?> v: ex.getConstraintViolations()) {
            error.addMessage(v.getPropertyPath().toString(), v.getMessage());
        }
        return error;
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<ErrorDTO>(new ErrorDTO(ex.getHttpStatus(), ex.getMessage()), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseEntity<?> handleAnyException(HttpServletRequest request, Throwable ex) {
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(new ErrorDTO(status, ex.getMessage()), status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

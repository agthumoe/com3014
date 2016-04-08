package com.surrey.com3014.group5.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Aung Thu Moe
 */
@ControllerAdvice(basePackages = {"com.surrey.com3014.group5.controllers"})
public class JSPExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSPExceptionHandler.class);
    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict(HttpServletRequest req, DataIntegrityViolationException exception, Model model) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);
        model.addAttribute("exception", exception);
        model.addAttribute("url", req.getRequestURL());
        return "error";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception exception, Model model) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);
        model.addAttribute("exception", exception);
        model.addAttribute("url", req.getRequestURL());
        return "error";
    }
}

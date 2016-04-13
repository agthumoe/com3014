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
 * This class handles exception and return jsp error page.
 *
 * @author Aung Thu Moe
 */
@ControllerAdvice(basePackages = {"com.surrey.com3014.group5.controllers"})
public class JSPExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSPExceptionHandler.class);

    /**
     * This method handles {@link DataIntegrityViolationException} and returns the error
     * page with http status (409)
     *
     * @param req       {@link HttpServletRequest}
     * @param exception {@link DataIntegrityViolationException} to be handled
     * @param model     Model attributes to be set
     * @return error page with http status 409
     */
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict(HttpServletRequest req, DataIntegrityViolationException exception, Model model) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);
        model.addAttribute("exception", exception);
        model.addAttribute("url", req.getRequestURL());
        return "error";
    }

    /**
     * This method handles any exception and return the error page with http status (500)
     *
     * @param req       {@link HttpServletRequest}
     * @param exception any exception to be handled
     * @param model     Model attributes to be set
     * @return error page with http status 500
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception exception, Model model) {
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);
        model.addAttribute("exception", exception);
        model.addAttribute("url", req.getRequestURL());
        return "error";
    }
}

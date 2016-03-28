package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Aung Thu Moe
 */
public class ResourceNotFoundException extends HttpStatusException {
    private static final long serialVersionUID = -3214197697877750405L;

    public ResourceNotFoundException() {
        this("The requested resource can not be found");
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * An exception with the HttpStatus code 404.
 *
 * @author Aung Thu Moe
 * @see HttpStatus#NOT_FOUND
 */
public class ResourceNotFoundException extends HttpStatusException {
    private static final long serialVersionUID = -3214197697877750405L;

    /**
     * A new ResourceNotFoundException with a default detailed message.
     */
    public ResourceNotFoundException() {
        this("The requested resource can not be found");
    }

    /**
     * A new ResourceNotFoundException with a specified detailed message.
     *
     * @param message
     */
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * An Exception with HttpStatus code 400.
 *
 * @author Aung Thu Moe
 * @see HttpStatus#BAD_REQUEST
 */
public class BadRequestException extends HttpStatusException {

    private static final long serialVersionUID = 5057103792289360217L;

    /**
     * A new BadRequestException with default detailed message.
     */
    public BadRequestException() {
        this("Server does not understand your request");
    }

    /**
     * A new BadRequestException with a specified detailed message.
     *
     * @param message The detailed message.
     */
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

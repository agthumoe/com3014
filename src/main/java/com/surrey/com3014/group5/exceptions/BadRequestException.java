package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Aung Thu Moe
 */
public class BadRequestException extends HttpStatusException {

    private static final long serialVersionUID = 5057103792289360217L;

    public BadRequestException() {
        this("Server does not understand your request");
    }

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

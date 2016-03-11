package com.surrey.com3014.group5.exceptions;

/**
 * @author Aung Thu Moe
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 6823164325855809534L;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}

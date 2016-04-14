package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * An exception which contains information regarding HttpStatus.
 * Default HttpStatus is 500 (Internal Server Error).
 *
 * @author Aung Thu Moe
 * @see HttpStatus
 */
public class HttpStatusException extends RuntimeException {
    private static final long serialVersionUID = 6823164325855809534L;

    /**
     * An HttpStatus code.
     */
    private final HttpStatus httpStatus;

    /**
     * Initialise HttpStatusException with the specified message with
     * HttpStatus code 500.
     *
     * @param message The detailed message
     */
    public HttpStatusException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Initialise HttpStatusException with the specified message and HttpStatus code.
     *
     * @param httpStatus The HttpStatus code
     * @param message    The detailed message
     */
    public HttpStatusException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    /**
     * Initialise HttpStatusException with the specified detailed message,
     * HttpStatus code and cause.
     *
     * @param httpStatus The HttpStatus code
     * @param message    The detailed message
     * @param cause      The cause
     */
    public HttpStatusException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

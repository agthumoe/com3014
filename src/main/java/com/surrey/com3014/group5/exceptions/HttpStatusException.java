package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Aung Thu Moe
 */
public class HttpStatusException extends RuntimeException {
    private static final long serialVersionUID = 6823164325855809534L;

    private final HttpStatus httpStatus;

    public HttpStatusException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatusException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatusException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
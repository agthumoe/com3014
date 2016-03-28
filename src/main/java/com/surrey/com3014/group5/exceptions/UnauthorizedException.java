package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Aung Thu Moe
 */
public class UnauthorizedException extends HttpStatusException {
    private static final long serialVersionUID = 9051743249194744787L;

    public UnauthorizedException() {
        this("You are not authorized to access the resource");
    }

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

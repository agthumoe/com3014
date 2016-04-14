package com.surrey.com3014.group5.exceptions;

import org.springframework.http.HttpStatus;

/**
 * An exception with HttpStatus code 401.
 *
 * @author Aung Thu Moe
 * @see HttpStatus#UNAUTHORIZED
 */
public class UnauthorizedException extends HttpStatusException {
    private static final long serialVersionUID = 9051743249194744787L;

    /**
     * A new UnauthorizedException with a default detailed message.
     */
    public UnauthorizedException() {
        this("You are not authorized to access the resource");
    }

    /**
     * A new UnauthorizedException with a specified detailed message.
     *
     * @param message The detailed message.
     */
    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

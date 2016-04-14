package com.surrey.com3014.group5.security;

/**
 * Constants for Spring Security Authorities.
 * This class should not be initialised.
 *
 * @author Aung Thu Moe
 */
public final class AuthoritiesConstants {
    /**
     * Administrator
     */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Generic User
     */
    public static final String USER = "ROLE_USER";

    /**
     * Anonymous User
     */
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * Just to prevent instantiation
     */
    private AuthoritiesConstants() {
    }
}

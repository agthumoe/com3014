package com.surrey.com3014.group5.security;

/**
 * @author Aung Thu Moe
 */
public enum Role {
    ADMIN ("ADMIN"),
    USER ("USER");

    private final String role;

    Role(final String role) {
        this.role = role;
    }

    public String toString() {
        return this.role;
    }
}

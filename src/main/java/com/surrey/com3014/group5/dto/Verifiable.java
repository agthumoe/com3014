package com.surrey.com3014.group5.dto;

/**
 * Extends this interface to allow current password validation.
 *
 * @author Aung Thu Moe
 */
public interface Verifiable {
    /**
     * Get current login password.
     *
     * @return current login password.
     */
    String getCurrentPassword();

    /**
     * Set current login password.
     *
     * @param currentPassword The current login password.
     */
    void setCurrentPassword(String currentPassword);
}

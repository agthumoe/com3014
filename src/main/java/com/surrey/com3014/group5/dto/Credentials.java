package com.surrey.com3014.group5.dto;

/**
 * Any class which extends this interface should have password and confirm password.
 *
 * @author Aung Thu Moe
 */
public interface Credentials {
    /**
     * Minimum password length.
     */
    int PASSWORD_MIN_LENGTH = 8;
    /**
     * Maximum password length.
     */
    int PASSWORD_MAX_LENGTH = 60;

    /**
     * Getter for password.
     *
     * @return password.
     */
    String getPassword();

    /**
     * Setter for password.
     *
     * @param password new password.
     */
    void setPassword(String password);

    /**
     * Getter for confirmPassword.
     *
     * @return confirmPassword.
     */
    String getConfirmPassword();

    /**
     * Setter for confirmPassword
     *
     * @param confirmPassword
     */
    void setConfirmPassword(String confirmPassword);
}

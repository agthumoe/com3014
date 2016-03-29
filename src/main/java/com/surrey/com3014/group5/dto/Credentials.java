package com.surrey.com3014.group5.dto;

/**
 * @author Aung Thu Moe
 */
public interface Credentials {
    int PASSWORD_MIN_LENGTH = 8;
    int PASSWORD_MAX_LENGTH = 60;
    void setPassword(String password);
    String getPassword();
    void setConfirmPassword(String confirmPassword);
    String getConfirmPassword();
}

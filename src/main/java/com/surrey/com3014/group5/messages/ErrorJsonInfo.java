package com.surrey.com3014.group5.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * Error info in json format.
 *
 * @author Aung Thu Moe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorJsonInfo {
    private final int status;
    private final String error;
    private final String exception;
    private final String message;
    private final Map<String, String> messages;

    public ErrorJsonInfo(HttpStatus status, Throwable exception) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.exception = exception.getClass().getName();
        this.message = null;
        this.messages = new HashMap<>();
    }

    public ErrorJsonInfo(HttpStatus status, Throwable exception, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.exception = exception.getClass().getName();
        this.message = message;
        this.messages = null;
    }

    public void addErrorMessage(String key, String value) {
        this.messages.put(key, value);
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getMessages() {
        return this.messages;
    }

}

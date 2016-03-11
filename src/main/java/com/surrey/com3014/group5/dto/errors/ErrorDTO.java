package com.surrey.com3014.group5.dto.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * This class is the based template to display error message in json format.<br>
 * The message contains Http status and its representation, the exception thrown,
 * and the error message.
 *
 * @author Aung Thu Moe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDTO implements Serializable {
    private static final long serialVersionUID = 2882194645276083077L;
    /**
     * Http status code
     */
    private final int status;
    /**
     * String representation of status code
     */
    private final String error;
    /**
     * Root cause of the problem
     */
    private final Throwable exception;
    /**
     * Specific details about the exception
     */
    private final String message;

    /**
     * Create error message.
     * @param status Http response status
     * @param exception the exception which has been thrown
     * @param message detailed error message
     */
    public ErrorDTO(HttpStatus status, Throwable exception, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.exception = exception;
        this.message = message;
    }

    /**
     * Get Http status code
     * @return Http status code
     */
    public final int getStatus() {
        return status;
    }

    /**
     * String representation of Http status code
     * @return string representation of Http status code
     */
    public final String getError() {
        return error;
    }

    /**
     * Get the root cause of the problem or exception in string representation
     * @return exception which has been thrown
     */
    @JsonProperty("exception")
    public final String getException() {
        return exception.getClass().toString();
    }

    /**
     * Get detailed error message of the exception
     * @return detailed error message
     */
    public String getMessage() {
        return message;
    }
}

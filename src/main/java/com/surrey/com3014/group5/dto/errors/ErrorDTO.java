package com.surrey.com3014.group5.dto.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * This class is the based template to display errors message in json format.<br>
 * The message contains Http status and its representation, the exception thrown,
 * and the errors message.
 *
 * @author Aung Thu Moe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDTO implements Serializable {
    private static final long serialVersionUID = 3765168795033103018L;
    /**
     * Http status code
     */
    private final int status;
    /**
     * String representation of status code
     */
    private final String error;

    /**
     * Specific details about the exception
     */
    private final String message;

    /**
     * Create errors message.
     * @param status Http response status
     * @param message detailed errors message
     */
    public ErrorDTO(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.error = status.getReasonPhrase();
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
     * Get detailed errors message of the exception
     * @return detailed errors message
     */
    public String getMessage() {
        return message;
    }
}

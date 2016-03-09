package com.surrey.com3014.group5.dto.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to display validation error message in proper json format.<br>
 * The validation error contains Http status and its representation, the exception thrown,
 * and the error messages are in key, value pair in which key is the property name and the
 * value will be the detailed error message.
 *
 * @author Aung Thu Moe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationErrorDTO extends ErrorDTO {

    private static final long serialVersionUID = -5490389937935689092L;
    /**
     * Specific details about the validation error message in key, value pairs.
     */
    private final Map<String, String> messages;

    /**
     * Create a new validation error message.
     * @param status Http response status
     * @param exception the exception which has been thrown
     */
    public ValidationErrorDTO(HttpStatus status, Throwable exception) {
        super(status, exception, null);
        this.messages = new HashMap<>();
    }

    /**
     * Add new validation error message.
     * @param propertyName which failed the validation
     * @param message detailed error message
     */
    public void addMessage(final String propertyName, final String message) {
        if (null == propertyName || null == message) throw new NullPointerException("key or value must not be null");
        this.messages.put(propertyName, message);
    }

    /**
     * Get validation error messages.
     * @return validation error messages
     */
    @JsonProperty("messages")
    public Map<String, String> getMessages() {
        return this.messages;
    }
}

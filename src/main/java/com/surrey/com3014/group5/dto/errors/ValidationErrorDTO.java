package com.surrey.com3014.group5.dto.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to display validation errors message in proper json format.<br>
 * The validation errors contains Http status and its representation, the exception thrown,
 * and the errors messages are in key, value pair in which key is the property name and the
 * value will be the detailed errors message.
 *
 * @author Aung Thu Moe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationErrorDTO extends ErrorDTO {

    /**
     * Specific details about the validation errors message in key, value pairs.
     */
    private final Map<String, String> messages;

    /**
     * Create a new validation errors message.
     * @param status Http response status
     */
    public ValidationErrorDTO(HttpStatus status) {
        super(status, null);
        this.messages = new HashMap<>();
    }

    /**
     * Add new validation errors message.
     * @param propertyName which failed the validation
     * @param message detailed errors message
     */
    public void addMessage(final String propertyName, final String message) {
        if (null == propertyName || null == message) throw new NullPointerException("key or value must not be null");
        this.messages.put(propertyName, message);
    }

    /**
     * Get validation errors messages.
     * @return validation errors messages
     */
    @JsonProperty("messages")
    public Map<String, String> getMessages() {
        return this.messages;
    }
}

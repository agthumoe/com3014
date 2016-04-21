package com.surrey.com3014.group5.websockets.dto;

import java.io.Serializable;

/**
 * Data transfer object for websocket messaging protocol.
 *
 * @author Aung Thu Moe
 */
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -5929392272209556479L;

    /**
     * Message body.
     */
    private String message;

    /**
     * Get the message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message body
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MessageDTO{" +
            "message='" + message + '\'' +
            '}';
    }
}

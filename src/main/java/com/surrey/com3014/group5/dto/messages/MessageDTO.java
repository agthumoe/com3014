package com.surrey.com3014.group5.dto.messages;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -5929392272209556479L;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "message='" + message + '\'' +
            '}';
    }
}

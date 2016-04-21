package com.surrey.com3014.group5.websockets.dto;

import java.io.Serializable;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
/**
 * @author Aung Thu Moe
 */
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -5929392272209556479L;

    private String message;

    public String getMessage() {
        return escapeHtml4(message);
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

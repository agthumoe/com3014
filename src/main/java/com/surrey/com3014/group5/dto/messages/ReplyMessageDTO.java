package com.surrey.com3014.group5.dto.messages;

import java.util.Date;

/**
 * @author Aung Thu Moe
 */
public class ReplyMessageDTO extends MessageDTO {
    private static final long serialVersionUID = 8048419213604886550L;

    private String username;
    private String currentTime;

    public ReplyMessageDTO() {
        super();
    }

    public ReplyMessageDTO(String username, String currentTime, String message) {
        this.username = username;
        this.currentTime = currentTime;
        this.setMessage(message);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "ReplyMessageDTO{" +
            "username='" + username + '\'' +
            ", currentTime=" + currentTime + '\'' +
            ", message=" + getMessage() +
            '}';
    }
}

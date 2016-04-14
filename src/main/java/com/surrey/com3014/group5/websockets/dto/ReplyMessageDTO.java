package com.surrey.com3014.group5.websockets.dto;

import com.surrey.com3014.group5.dto.users.UserDTO;

/**
 * @author Aung Thu Moe
 */
public class ReplyMessageDTO extends MessageDTO {
    private static final long serialVersionUID = 8048419213604886550L;

    private UserDTO user;
    private String currentTime;

    public ReplyMessageDTO() {
        super();
    }

    public ReplyMessageDTO(UserDTO user, String currentTime, String message) {
        this.user = user;
        this.currentTime = currentTime;
        this.setMessage(message);
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
            "user='" + user.toString() + '\'' +
            ", currentTime=" + currentTime + '\'' +
            ", message=" + getMessage() +
            '}';
    }
}

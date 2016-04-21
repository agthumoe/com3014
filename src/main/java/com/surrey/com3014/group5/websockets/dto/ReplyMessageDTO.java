package com.surrey.com3014.group5.websockets.dto;

import com.surrey.com3014.group5.dto.users.UserDTO;

/**
 * Data transfer object for messaging protocol.
 *
 * @author Aung Thu Moe
 */
public class ReplyMessageDTO extends MessageDTO {
    private static final long serialVersionUID = 8048419213604886550L;

    /**
     * User who reply this message.
     */
    private UserDTO user;
    /**
     * Message sent time.
     */
    private String currentTime;

    /**
     * Default constructor.
     */
    public ReplyMessageDTO() {
        super();
    }

    /**
     * Initialise a new ReplyMessageDTO from the specified user, current time and message.
     *
     * @param user        current user principal
     * @param currentTime current time as milliseconds.
     * @param message     reply message.
     */
    public ReplyMessageDTO(UserDTO user, String currentTime, String message) {
        this.user = user;
        this.currentTime = currentTime;
        this.setMessage(message);
    }

    /**
     * Get user of this reply message.
     *
     * @return user of this reply message.
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Set user of this reply message.
     *
     * @param user of this reply message.
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Get message sent time in milliseconds.
     *
     * @return message sent time in milliseconds.
     */
    public String getCurrentTime() {
        return currentTime;
    }

    /**
     * Set message sent time in milliseconds.
     *
     * @param currentTime message sent time in milliseconds.
     */
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ReplyMessageDTO{" +
            "user='" + user.toString() + '\'' +
            ", currentTime=" + currentTime + '\'' +
            ", message=" + getMessage() +
            '}';
    }
}

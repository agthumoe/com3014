package com.surrey.com3014.group5.websockets.dto;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.domains.EloRating;
import com.surrey.com3014.group5.websockets.domains.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Data transfer object regarding game player.
 *
 * @author Aung Thu Moe
 */
public class PlayerDTO extends UserDTO implements EloRating {
    /**
     * Challenger role label.
     */
    public static final String CHALLENGER = "CHALLENGER";
    /**
     * Challenged role label.
     */
    public static final String CHALLENGED = "CHALLENGED";
    private static final long serialVersionUID = -1223709935605109559L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDTO.class);
    /**
     * Role of this player, Challenger or Challenged.
     */
    private String role;
    /**
     * Resolution of this player's device.
     */
    private Resolution resolution = null;
    /**
     * Flag to identify if the player is ready to play or not.
     */
    private boolean ready = false;
    /**
     * Default starting Elo rating.
     */
    private double rating = 1500;
    /**
     * message sent time in milliseconds to calculate ping rate.
     */
    private long messageSentTime;
    /**
     * message received time in milliseconds to calculate ping rate.
     */
    private long messageReceivedTime;

    /**
     * Create a new player dto from User object.
     *
     * @param user User model of this player.
     */
    public PlayerDTO(User user) {
        super(user);
        this.rating = user.getLeaderboard().getRating();
    }

    /**
     * Create a new player dto from user object with the specified role.
     *
     * @param user User model of this player.
     * @param role current palyer role.
     */
    public PlayerDTO(User user, String role) {
        this(user);
        this.role = role;
        this.ready = false;
    }

    /**
     * Get message sent time in milliseconds
     *
     * @return messge sent time in milliseconds.
     */
    public long getMessageSentTime() {
        return messageSentTime;
    }

    /**
     * Set message sent time in milliseconds.
     *
     * @param messageSentTime in milliseconds.
     */
    public void setMessageSentTime(long messageSentTime) {
        this.messageSentTime = messageSentTime;
    }

    /**
     * Get message received time in milliseconds
     *
     * @return message received time in milliseconds.
     */
    public long getMessageReceivedTime() {
        return messageReceivedTime;
    }

    /**
     * Set message receive time in milliseconds.
     *
     * @param messageReceivedTime in milliseconds.
     */
    public void setMessageReceivedTime(long messageReceivedTime) {
        this.messageReceivedTime = messageReceivedTime;
    }

    /**
     * Calcualte the get the ping rate of this player.
     *
     * @return estimated ping rate of this player.
     */
    public long getPingRate() {
        return (this.messageReceivedTime - this.messageSentTime) / 2;
    }

    /**
     * Get resolution of this plyer's device.
     *
     * @return resolution of this player's device.
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * Set resolution of this player's device.
     *
     * @param resolution of this player's device.
     */
    public void setResolution(Resolution resolution) {
        Assert.notNull(resolution);
        if (this.resolution != null) {
            LOGGER.warn("Resolution has already set");
        }
        this.resolution = resolution;
    }

    /**
     * Get current player's role.
     *
     * @return current player's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Set current player's role.
     *
     * @param role current player's role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Check if the player if ready to play.
     *
     * @return true if the player is ready to play.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Set the player to be ready.
     *
     * @param ready to player game.
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", username=" + getUsername() +
            ", email=" + getEmail() +
            ", name=" + getName() +
            ", role=" + role +
            ", resolution=" + resolution +
            ", ready=" + ready +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRating() {
        return (double) Math.round(this.rating * 100.0) / 100.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getUserId() {
        return getId();
    }

}

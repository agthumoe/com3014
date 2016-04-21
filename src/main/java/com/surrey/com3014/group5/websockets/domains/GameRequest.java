package com.surrey.com3014.group5.websockets.domains;

import com.surrey.com3014.group5.models.impl.User;

import java.io.Serializable;

/**
 * This class represents a game request sent by a user.
 *
 * @author Aung Thu Moe
 */
public class GameRequest implements Serializable {
    private static final long serialVersionUID = -5340436566899111089L;
    /**
     * Default game request timeout duration.
     */
    private static final int TIMEOUT = 10000;
    /**
     * The requested time.
     */
    private final long requestedTime;
    /**
     * Current game id.
     */
    private String gameID;
    /**
     * The player who sent this game request.
     */
    private User challenger;
    /**
     * The player who has been challenged.
     */
    private User challenged;

    /**
     * Initialise a new game request with current time as requestedTime.
     */
    public GameRequest() {
        this.requestedTime = System.currentTimeMillis();
    }

    /**
     * Get current game id.
     *
     * @return current game id.
     */
    public String getGameID() {
        return gameID;
    }

    /**
     * Set current game id.
     *
     * @param gameID current game id.
     */
    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    /**
     * Get the challenger of this game request.
     *
     * @return challenger of this game request.
     */
    public User getChallenger() {
        return challenger;
    }

    /**
     * Set the challenger of this game request.
     *
     * @param challenger of this game request.
     */
    public void setChallenger(User challenger) {
        this.challenger = challenger;
    }

    /**
     * Get the challenged of this game request.
     *
     * @return challenged of this game request.
     */
    public User getChallenged() {
        return challenged;
    }

    /**
     * Set the challenged of this game request.
     *
     * @param challenged of this game request.
     */
    public void setChallenged(User challenged) {
        this.challenged = challenged;
    }

    /**
     * Get the requested time of this game request.
     *
     * @return requested time in milliseconds.
     */
    public long getRequestedTime() {
        return requestedTime;
    }

    /**
     * Check if the game is expired or not.
     *
     * @return true if the game is expired, false otherwise.
     */
    public boolean isExpired() {
        return (System.currentTimeMillis() - this.requestedTime) > TIMEOUT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GameRequest{" +
            "gameID='" + gameID + '\'' +
            ", requestedTime=" + requestedTime +
            ", challenger=" + challenger +
            ", challenged=" + challenged +
            ", expired=" + isExpired() +
            '}';
    }
}

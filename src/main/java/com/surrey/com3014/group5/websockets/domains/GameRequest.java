package com.surrey.com3014.group5.websockets.domains;

import com.surrey.com3014.group5.models.impl.User;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Aung Thu Moe
 */
public class GameRequest implements Serializable {
    private static final long serialVersionUID = -5340436566899111089L;
    private static final int TIMEOUT = 10000;
    private String gameID;
    private final AtomicLong requestedTime;
    private User challenger;
    private User challenged;

    public GameRequest() {
        this.requestedTime = new AtomicLong(System.currentTimeMillis());
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public User getChallenger() {
        return challenger;
    }

    public void setChallenger(User challenger) {
        this.challenger = challenger;
    }

    public User getChallenged() {
        return challenged;
    }

    public void setChallenged(User challenged) {
        this.challenged = challenged;
    }

    public AtomicLong getRequestedTime() {
        return requestedTime;
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() - this.requestedTime.get()) > TIMEOUT;
    }

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

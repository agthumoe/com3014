package com.surrey.com3014.group5.game;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Aung Thu Moe
 */
public class GameRequest implements Serializable {
    private static final long serialVersionUID = -5340436566899111089L;
    private String gameID;
    private final AtomicLong requestedTime;
    private UserDTO challenger;
    private UserDTO challenged;

    public GameRequest() {
        this.requestedTime = new AtomicLong(System.currentTimeMillis());
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public UserDTO getChallenger() {
        return challenger;
    }

    public void setChallenger(User challenger) {
        this.challenger = new UserDTO(challenger);
    }

    public UserDTO getChallenged() {
        return challenged;
    }

    public void setChallenged(User challenged) {
        this.challenged = new UserDTO(challenged);
    }

    public AtomicLong getRequestedTime() {
        return requestedTime;
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() - this.requestedTime.get()) > 30000;
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

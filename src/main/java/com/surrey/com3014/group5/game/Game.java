package com.surrey.com3014.group5.game;

import com.surrey.com3014.group5.dto.users.GamerDTO;
import org.springframework.security.access.AccessDeniedException;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class Game implements Serializable {
    private static final long serialVersionUID = -5340436566899111089L;
    public static final int TIME_TO_START = 5000;
    private String gameID;
    private GamerDTO challenger;
    private GamerDTO challenged;
    private boolean isExpired = false;

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public GamerDTO getChallenger() {
        return challenger;
    }

    public void setChallenger(GamerDTO challenger) {
        this.challenger = challenger;
    }

    public GamerDTO getChallenged() {
        return challenged;
    }

    public GamerDTO getCurrentPlayer(long id) {
        if (id == challenger.getId()) {
            return challenger;
        } else if (id == challenged.getId()) {
            return challenged;
        } else {
            throw new AccessDeniedException("Unauthorised user trying to access the game");
        }
    }

    public GamerDTO getOppositePlayer(long id) {
        if (getCurrentPlayer(id).getRole().equals(GamerDTO.CHALLENGER)) {
            return challenged;
        } else if (getCurrentPlayer(id).getRole().equals(GamerDTO.CHALLENGED)) {
            return challenger;
        }
        throw new AccessDeniedException("Unauthorised user trying to access the game");
    }

    public void setChallenged(GamerDTO challenged) {
        this.challenged = challenged;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return gameID.equals(game.gameID);

    }

    @Override
    public int hashCode() {
        return gameID.hashCode();
    }

    @Override
    public String toString() {
        return "Game{" +
            "gameID='" + gameID + '\'' +
            ", challenger=" + challenger +
            ", challenged=" + challenged +
            ", isExpired=" + isExpired +
            '}';
    }

    public void setGamerResolution(long userID, int height, int width) {
        GamerDTO gamer = this.getCurrentPlayer(userID);
        // update the screen resolution of the client
        Resolution resolution = new Resolution(height, width);
        gamer.setResolution(resolution);
    }

    public Resolution getResolution() {
        Resolution challengerResolution = this.getChallenger().getResolution();
        Resolution challengedResolution = this.getChallenged().getResolution();
        if (challengerResolution == null || challengedResolution == null) {
            return null;
        }
        int bestHeight = challengerResolution.getHeight() < challengedResolution.getHeight() ? challengerResolution.getHeight() : challengedResolution.getHeight();
        int bestWidth = challengerResolution.getWidth() < challengedResolution.getWidth() ? challengerResolution.getWidth() : challengedResolution.getWidth();
        return new Resolution(bestHeight, bestWidth);
    }
}

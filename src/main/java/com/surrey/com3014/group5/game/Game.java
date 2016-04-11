package com.surrey.com3014.group5.game;

import com.surrey.com3014.group5.dto.users.GamerDTO;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class Game implements Serializable {
    private static final long serialVersionUID = -5340436566899111089L;
    private String gameID;
    private GamerDTO challenger;
    private GamerDTO challenged;

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

    public void setChallenged(GamerDTO challenged) {
        this.challenged = challenged;
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
            '}';
    }
}

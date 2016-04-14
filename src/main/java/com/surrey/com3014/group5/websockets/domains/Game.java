package com.surrey.com3014.group5.websockets.domains;

import com.surrey.com3014.group5.websockets.dto.PlayerDTO;
import org.springframework.security.access.AccessDeniedException;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
public class Game implements Serializable {
//    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static final long serialVersionUID = -5340436566899111089L;
    public static final int TIME_TO_START = 5000;
    private String gameID;
    private PlayerDTO challenger;
    private PlayerDTO challenged;
    private boolean expired = false;
    private boolean started = false;

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public PlayerDTO getChallenger() {
        return challenger;
    }

    public void setChallenger(PlayerDTO challenger) {
        this.challenger = challenger;
    }

    public PlayerDTO getChallenged() {
        return challenged;
    }

    public PlayerDTO getCurrentPlayer(long id) {
        if (id == challenger.getId()) {
            return challenger;
        } else if (id == challenged.getId()) {
            return challenged;
        } else {
            throw new AccessDeniedException("Unauthorised user trying to access the game");
        }
    }

    public PlayerDTO getOppositePlayer(long id) {
        if (getCurrentPlayer(id).getRole().equals(PlayerDTO.CHALLENGER)) {
            return challenged;
        } else if (getCurrentPlayer(id).getRole().equals(PlayerDTO.CHALLENGED)) {
            return challenger;
        }
        throw new AccessDeniedException("Unauthorised user trying to access the game");
    }

    public void setChallenged(PlayerDTO challenged) {
        this.challenged = challenged;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
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
            ", expired=" + expired +
            ", started=" + started +
            '}';
    }

    public void setPlayerResolution(long userID, int height, int width) {
        PlayerDTO player = this.getCurrentPlayer(userID);
        // update the screen resolution of the client
        Resolution resolution = new Resolution(height, width);
        player.setResolution(resolution);
    }

    public Optional<Resolution> getResolution() {
        Optional<Resolution> challengerResolution = this.getChallenger().getResolution();
        Optional<Resolution> challengedResolution = this.getChallenged().getResolution();
        if (challengerResolution.isPresent() && challengedResolution.isPresent()) {
            int bestHeight = challengerResolution.get().getHeight() < challengedResolution.get().getHeight() ? challengerResolution.get().getHeight() : challengedResolution.get().getHeight();
            int bestWidth = challengerResolution.get().getWidth() < challengedResolution.get().getWidth() ? challengerResolution.get().getWidth() : challengedResolution.get().getWidth();
            return Optional.of(new Resolution(bestHeight, bestWidth));
        }
        return Optional.empty();
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}

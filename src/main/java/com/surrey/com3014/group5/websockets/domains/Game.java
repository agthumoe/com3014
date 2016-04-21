package com.surrey.com3014.group5.websockets.domains;

import com.surrey.com3014.group5.websockets.dto.PlayerDTO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Optional;

/**
 * Main class for tron game.
 *
 * @author Aung Thu Moe
 */
public class Game implements Serializable {

    /**
     * Delay to start the game.
     */
    public static final int TIME_TO_START = 5000;
    private static final long serialVersionUID = -5340436566899111089L;
    /**
     * Current game id.
     */
    private String gameID;
    /**
     * Challenger of this game.
     */
    private PlayerDTO challenger;
    /**
     * Challenged of this game.
     */
    private PlayerDTO challenged;
    /**
     * If the game is expired.
     */
    private boolean expired = false;
    /**
     * If the game has started.
     */
    private boolean started = false;

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
     * Get the challenger of this game.
     *
     * @return challenger of this game.
     */
    public PlayerDTO getChallenger() {
        return challenger;
    }

    /**
     * Set the challenger of this game.
     *
     * @param challenger of this game.
     */
    public void setChallenger(PlayerDTO challenger) {
        this.challenger = challenger;
    }

    /**
     * Get the challenged of this game.
     *
     * @return challenged of this game.
     */
    public PlayerDTO getChallenged() {
        return challenged;
    }

    /**
     * Set the challenged of this game.
     *
     * @param challenged of this game.
     */
    public void setChallenged(PlayerDTO challenged) {
        this.challenged = challenged;
    }

    /**
     * Get the player of this id.<br> Use this method if the user id is provided and
     * the role (challenger or challenged) of the player is not known. But the user of this id must be
     * associated with this game.
     *
     * @param id of the player.
     * @return the palyer of the this id.
     * @throws AccessDeniedException if the user of this id is neither challenger nor challenged.
     */
    public PlayerDTO getCurrentPlayer(long id) {
        Assert.notNull(this.challenger, "class field: challenger hasn't been set yet");
        Assert.notNull(this.challenged, "class field: challenged hasn't been set yet");
        if (id == challenger.getId()) {
            return challenger;
        } else if (id == challenged.getId()) {
            return challenged;
        } else {
            throw new AccessDeniedException("Unauthorised user trying to access the game");
        }
    }

    /**
     * Get the opposite player playing against this user of id. <br> Use this method if the user id is provided,
     * but the role (challenger or challenged) of the player is not known. But the user of this id must be associated
     * with this game.
     *
     * @param id of current user.
     * @return the opposite player playing against the user of the provided id.
     * @throws AccessDeniedException if the user of this id is neither challenger nor challenged.
     */
    public PlayerDTO getOppositePlayer(long id) {
        if (getCurrentPlayer(id).getRole().equals(PlayerDTO.CHALLENGER)) {
            return challenged;
        } else if (getCurrentPlayer(id).getRole().equals(PlayerDTO.CHALLENGED)) {
            return challenger;
        }
        throw new AccessDeniedException("Unauthorised user trying to access the game");
    }

    /**
     * Check if the game is expired or not.
     *
     * @return true if the game is expired, false otherwise.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Set if the game is expired or not.
     *
     * @param expired true if the game is expired, false otherwise.
     */
    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Game game = (Game) o;

        return gameID.equals(game.gameID);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return gameID.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Game{"
            + "gameID='" + gameID + '\''
            + ", challenger=" + challenger
            + ", challenged=" + challenged
            + ", expired=" + expired
            + ", started=" + started
            + '}';
    }

    /**
     * Set the player's resolution. The provided player's id must be associated with this game.
     *
     * @param userID Player's id
     * @param height height of the resolution.
     * @param width  width of the resolution.
     * @throws if the userID is neither challenger nor challenged.
     */
    public void setPlayerResolution(long userID, int height, int width) {
        PlayerDTO player = this.getCurrentPlayer(userID);
        // update the screen resolution of the client
        Resolution resolution = new Resolution(height, width);
        player.setResolution(resolution);
    }

    /**
     * Get the most suitable resolution for both players, which is the smallest resolution.
     *
     * @return the most suitable resolution for both players.
     */
    public Optional<Resolution> getResolution() {
        Resolution challengerResolution = this.getChallenger().getResolution();
        Resolution challengedResolution = this.getChallenged().getResolution();
        if (challengerResolution != null && challengedResolution != null) {
            int bestHeight = challengerResolution.getHeight() < challengedResolution.getHeight() ? challengerResolution.getHeight() : challengedResolution.getHeight();
            int bestWidth = challengerResolution.getWidth() < challengedResolution.getWidth() ? challengerResolution.getWidth() : challengedResolution.getWidth();
            return Optional.of(new Resolution(bestHeight, bestWidth));
        }
        return Optional.empty();
    }

    /**
     * Check if the game has been started.
     *
     * @return true if the game has been started, false otherwise.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Set the game as started or not.
     *
     * @param started true if the game has been started, false otherwise.
     */
    public void setStarted(boolean started) {
        this.started = started;
    }
}

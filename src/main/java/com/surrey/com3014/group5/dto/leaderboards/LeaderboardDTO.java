package com.surrey.com3014.group5.dto.leaderboards;

import com.surrey.com3014.group5.dto.users.UserDTO;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * Data transfer object for leaderboard.
 *
 * @author Aung Thu Moe
 */
@ApiModel
public class LeaderboardDTO implements Serializable {
    private static final long serialVersionUID = 4783678694843808055L;

    /**
     * User of this leaderboard.
     */
    private UserDTO user;
    /**
     * Total number of game won.
     */
    private long wins;
    /**
     * Total number of game lost.
     */
    private long losses;
    /**
     * Current user's elo rating.
     */
    private double rating;

    /**
     * Default constructor initialise leaderboard dto object.
     */
    public LeaderboardDTO() {
        super();
    }

    /**
     * Initliase new LeaderboardDTO object from provided parameters.
     *
     * @param user   User of this leaderboard.
     * @param wins   total number of games won.
     * @param losses total number of games lost.
     * @param rating current user's elo rating.
     */
    public LeaderboardDTO(UserDTO user, long wins, long losses, double rating) {
        this.user = user;
        this.wins = wins;
        this.losses = losses;
        this.rating = rating;
    }

    /**
     * Get user of this leaderboard.
     *
     * @return user of this leaderboard.
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Set user of this leaderboard.
     *
     * @param user of this leaderboard.
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Get total number of games won.
     *
     * @return total number of games won.
     */
    public long getWins() {
        return wins;
    }

    /**
     * Set total number of games won.
     *
     * @param wins total number of games won.
     */
    public void setWins(long wins) {
        this.wins = wins;
    }

    /**
     * Get total number of games lost.
     *
     * @return total number of games lost.
     */
    public long getLosses() {
        return losses;
    }

    /**
     * Set total number of games lost.
     *
     * @param losses total number of games lost.
     */
    public void setLosses(long losses) {
        this.losses = losses;
    }

    /**
     * Get current user's elo rating.
     *
     * @return current user's elo rating.
     */
    public double getRating() {
        return (double) Math.round(this.rating * 100.0) / 100.0;
    }

    /**
     * Set current user's elo rating.
     *
     * @param rating current user's elo rating.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "LeaderboardDTO{" +
            ", user='" + user + '\'' +
            ", wins=" + wins +
            ", losses=" + losses +
            ", rating=" + rating +
            '}';
    }
}

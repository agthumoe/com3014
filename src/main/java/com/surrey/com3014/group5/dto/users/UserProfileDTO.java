package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;

/**
 * Data transfer object to display user's profile.
 *
 * @author Aung Thu Moe
 */
public class UserProfileDTO extends UserDTO {
    private static final long serialVersionUID = 3069218550541349354L;
    /**
     * Number of games this user has won.
     */
    private long wins;
    /**
     * Number of games this user has lost.
     */
    private long losses;
    /**
     * This user's elo rating.
     */
    private double rating;

    /**
     * Initialise new UserProfileDTO object from User model
     *
     * @param user User model
     */
    public UserProfileDTO(User user) {
        super(user);
        final Leaderboard leaderboard = user.getLeaderboard();
        this.wins = leaderboard.getWins();
        this.losses = leaderboard.getLosses();
        this.rating = leaderboard.getRating();
    }

    /**
     * Get number of games this user has won.
     *
     * @return number of games this user has won.
     */
    public long getWins() {
        return wins;
    }

    /**
     * Set number of games this user has won.
     *
     * @param wins number of games this user has won.
     */
    public void setWins(long wins) {
        this.wins = wins;
    }

    /**
     * Get number of games this user has lost.
     *
     * @return number of games this user has lost.
     */
    public long getLosses() {
        return losses;
    }

    /**
     * Set number of games this user has lost.
     *
     * @param losses number of games this user has lost.
     */
    public void setLosses(long losses) {
        this.losses = losses;
    }

    /**
     * Get this user's elo rating.
     *
     * @return this user's elo rating.
     */
    public double getRating() {
        return (double) Math.round(this.rating * 100.0) / 100.0;
    }

    /**
     * Set this user's elo rating.
     *
     * @param rating elo rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", name='" + getName() + '\'' +
            ", wins=" + wins +
            ", losses=" + losses +
            ", rating=" + rating +
            '}';
    }
}

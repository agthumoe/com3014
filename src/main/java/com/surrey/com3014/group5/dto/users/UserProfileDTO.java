package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aung Thu Moe
 */
public class UserProfileDTO extends UserDTO {
    private static final long serialVersionUID = 3069218550541349354L;
    private long wins;
    private long losses;
    private double rating;
    private static Logger LOGGER = LoggerFactory.getLogger(UserProfileDTO.class);

    public UserProfileDTO(User user) {
        super(user);
        final Leaderboard leaderboard = user.getLeaderboard();
        this.wins = leaderboard.getWins();
        this.losses = leaderboard.getLosses();
        this.rating = leaderboard.getRating();
        LOGGER.debug(this.toString());
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    public long getLosses() {
        return losses;
    }

    public void setLosses(long losses) {
        this.losses = losses;
    }

    public double getRating() {
        return (double) Math.round(this.rating * 100.0) / 100.0;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

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

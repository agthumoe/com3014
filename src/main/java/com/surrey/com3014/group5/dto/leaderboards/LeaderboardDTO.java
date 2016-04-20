package com.surrey.com3014.group5.dto.leaderboards;

import com.surrey.com3014.group5.dto.users.UserDTO;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * @author Aung Thu Moe
 */
@ApiModel
public class LeaderboardDTO implements Serializable {
    private static final long serialVersionUID = 4783678694843808055L;

    private UserDTO user;
    private long wins;
    private long losses;
    private double rating;

    public LeaderboardDTO() {
        super();
    }

    public LeaderboardDTO(UserDTO user, long wins, long losses, double rating) {
        this.user = user;
        this.wins = wins;
        this.losses = losses;
        this.rating = rating;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    public String getRating() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(this.rating);

    }

    public void setRating(double rating) {
        this.rating = rating;
    }

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

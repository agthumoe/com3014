package com.surrey.com3014.group5.dto;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
@ApiModel
public class LeaderboardDTO implements Serializable {
    private static final long serialVersionUID = 4783678694843808055L;

    private String username;
    private long wins;
    private long losses;
    private double ratio;

    public LeaderboardDTO() {
        super();
    }

    public LeaderboardDTO(String username, long wins, long losses, double ratio) {
        this.username = username;
        this.wins = wins;
        this.losses = losses;
        this.ratio = ratio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "LeaderboardDTO{" +
            ", username='" + username + '\'' +
            ", wins=" + wins +
            ", losses=" + losses +
            ", ratio=" + ratio +
            '}';
    }
}

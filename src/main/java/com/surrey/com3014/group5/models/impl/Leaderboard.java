package com.surrey.com3014.group5.models.impl;

import com.surrey.com3014.group5.models.MutableModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Spyros Balkonis
 */
@Entity
@Table(name = "leaderboard")
public class Leaderboard extends MutableModel{
    private static final long serialVersionUID = 5668537543274150457L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OrderBy("username")
    private User user;

    @Column(nullable = false)
    @NotNull
    private long wins = 0;

    @Column(nullable = false)
    @NotNull
    private long losses = 0;

    @Column(nullable = false)
    @NotNull
    private double ratio = 0.000;

    public Leaderboard() {
        super();
    }

    public Leaderboard(User user, long wins, long losses, double ratio){
        super();
        this.user = user;
        this.wins = wins;
        this.losses = losses;
        this.ratio = ratio;
    }

    public Leaderboard(User user, long wins, long losses){
        super();
        this.user = user;
        this.wins = wins;
        this.losses = losses;
        if(this.losses != 0) {
            this.ratio = this.wins / this.losses;
        }else {
            this.ratio = this.wins;
        }
    }

    public Leaderboard(User user){
        super();
        this.user = user;
        this.wins = 0;
        this.losses = 0;
        this.ratio = 0.000;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
            "user=" + user +
            ", wins=" + wins +
            ", losses=" + losses +
            ", ratio=" + ratio +
            '}';
    }

}

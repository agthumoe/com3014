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

    private static final long serialVersionUID = -2664947233441065553L;

    @OneToOne(mappedBy = "leaderboard", cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false, updatable = false)
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
    }

    public Leaderboard(User user, long wins, long losses, double ratio){
        this.user = user;
        this.wins = wins;
        this.losses = losses;
        this.ratio = ratio;
    }

    public Leaderboard(User user, long wins, long losses){
        this.user = user;
        this.wins = wins;
        this.losses = losses;
        this.ratio = this.wins / this.losses;
    }

    public Leaderboard(User user){
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

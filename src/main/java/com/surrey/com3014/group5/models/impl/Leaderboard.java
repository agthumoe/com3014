package com.surrey.com3014.group5.models.impl;

import com.surrey.com3014.group5.models.MutableModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Spyros Balkonis
 */
@Entity
@Table(name = "leaderboard")
public class Leaderboard extends MutableModel {
    private static final long serialVersionUID = 5668537543274150457L;

    /**
     * user of this leaderboard
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    /**
     * Number of battle that user has won.
     */
    @Column(nullable = false)
    @NotNull
    private long wins = 0;

    /**
     * Number of battle that user has lost.
     */
    @Column(nullable = false)
    @NotNull
    private long losses = 0;

    /**
     * Ratio = wins / (wins + losses)
     */
    @Column(nullable = false)
    @NotNull
    private double ratio = 0.000;

    /**
     * Default constructor initialise this leaderboard object.
     */
    public Leaderboard() {
        super();
    }

    /**
     * Create a leadboard of the provided user.
     *
     * @param user of this leaderboard.
     */
    public Leaderboard(User user) {
        super();
        this.user = user;
        this.wins = 0;
        this.losses = 0;
        this.ratio = 0.000;
    }

    /**
     * Get user of this leaderboard.
     *
     * @return user of this leaderboard.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set user of this leaderboard.
     *
     * @param user of this leaderboard.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get number of battle that user has won.
     *
     * @return number of battle that user has won.
     */
    public long getWins() {
        return wins;
    }

    /**
     * Set number of battle that user has won.
     *
     * @param wins number of battle that user has won.
     */
    public void setWins(long wins) {
        this.wins = wins;
    }

    /**
     * Get number of battle that user has lost.
     *
     * @return number of battle that user has lost.
     */
    public long getLosses() {
        return losses;
    }

    /**
     * Set number of battle that user has lost.
     *
     * @param losses number of battle that user has lost.
     */
    public void setLosses(long losses) {
        this.losses = losses;
    }

    /**
     * Get win/loss ratio.
     *
     * @return win/loss ratio
     * @see #ratio
     */
    public double getRatio() {
        return ratio;
    }

    /**
     * Set win/loss ratio.
     *
     * @param ratio win/loss ratio
     * @see #ratio
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Leaderboard{" +
            "user=" + user +
            "id='" + getId() + '\'' +
            "createdDate='" + getCreatedDate() + '\'' +
            "lastModifiedDate='" + getLastModifiedDate() + '\'' +
            ", wins=" + wins +
            ", losses=" + losses +
            ", ratio=" + ratio +
            '}';
    }

}

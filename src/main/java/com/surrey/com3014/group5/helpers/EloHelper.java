package com.surrey.com3014.group5.helpers;

/**
 * Elo helper to calculate game ranking. We got the logic and the code from the provided links.
 *
 * @author Spyridon Balkonis
 * @author Aung Thu Moe
 * @see <a href="https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/">How to calculate elo rating</a>
 * @see <a href="https://github.com/tladyman/JavaElo/blob/master/src/main/java/com/github/tladyman/elo/Elo.java">We get the code from here</a>
 */
public class EloHelper {

    //The maximum possible adjustment per game (K-Factor)
    public static final double kFactor = 32.0;
    private double rating;

    /**
     * No args constructor. 0 initial rating, 0 games
     */
    public EloHelper() {
        this(0);
    }

    /**
     * Constructor with rating setting. 0 games
     *
     * @param rating The initial rating of the player.
     */
    public EloHelper(double rating) {
        this.rating = rating;
    }

    /**
     * A helper method to adjust the scores of a winner and loser.
     *
     * @param winner The player that won
     * @param loser  The player that lost
     */
    public static void adjust(EloHelper winner, EloHelper loser) {
        winner.adjustFromWin(loser);
        loser.adjustFromLoss(winner);
    }

    /**
     * Get the rating of this player.
     *
     * @return The rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set the rating of this player
     *
     * @param rating The rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Calculate the expectation this player will win vs another opponent.
     *
     * @param opponent The opponent to play
     * @return The probability (0-1) this player will beat the opponent.
     */
    public double calculateExpectation(EloHelper opponent) {
        return 1 / (1 + Math.pow(10, (opponent.getRating() - this.getRating()) / 400));
    }

    /**
     * Calculate and return the value of the new rating should this player beat
     * the opponent.
     *
     * @param opponent The opponent to play
     * @return The value the new rating would take
     */
    public double newRatingWin(EloHelper opponent) {
        return this.rating + (kFactor * (1 - this.calculateExpectation(opponent)));
    }

    /**
     * Calculate and return the value of the new rating should this player lose
     * to the opponent.
     *
     * @param opponent The opponent to play
     * @return The value the new rating would take
     */
    public double newRatingLoss(EloHelper opponent) {
        return this.rating + (kFactor * (0 - this.calculateExpectation(opponent)));
    }

    /**
     * Adjust this players rating based on beating the opponent.
     *
     * @param opponent The opponent to play
     */
    public void adjustFromWin(EloHelper opponent) {
        this.setRating(this.newRatingWin(opponent));
    }

    /**
     * Adjust this players rating based on losing to the opponent.
     *
     * @param opponent The opponent to play
     */
    public void adjustFromLoss(EloHelper opponent) {
        this.setRating(this.newRatingLoss(opponent));
    }

}

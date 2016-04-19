package helpers;

/**
 * @author Spyridon Balkonis
 * @author Aung Thu Moe
 */
public class EloHelper {

    //The maximum possible adjustment per game (K-Factor)
    public static final double kFactor = 32.0;
    //Flag whether to keep a running total of the game count
    private final boolean countGames;

    private double rating;
    private long gamesPlayed;

    /**
     * No args constructor. 0 initial rating, 0 games, count games.
     */
    public EloHelper() {
        this(0);
    }

    /**
     * Constructor with rating setting. 0 games, count games.
     *
     * @param rating The initial rating of the player.
     */
    public EloHelper(double rating) {
        this(rating, 0);
    }

    /**
     * Constructor with rating and game count setting. Will count games.
     *
     * @param rating The initial rating of the player.
     * @param gamesPlayed The number of games the player has played to achieve
     * this rating.
     */
    public EloHelper(double rating, long gamesPlayed) {
        this(rating, gamesPlayed, true);
    }

    /**
     * Constructor to set all params.
     *
     * @param rating The initial rating of the player.
     * @param gamesPlayed The number of games the player has played to achieve
     * this rating.
     * @param countGames Boolean to keep a running total of games or not.
     */
    public EloHelper(double rating, long gamesPlayed, boolean countGames) {
        this.rating = rating;
        this.gamesPlayed = gamesPlayed;
        this.countGames = countGames;
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
     * Get the total number of games played.
     *
     * @return The number of games
     */
    public long getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Set the total number of games played.
     *
     * @param gamesPlayed The number of games
     */
    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
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
        if (countGames) {
            gamesPlayed++;
        }
        this.setRating(this.newRatingWin(opponent));
    }

    /**
     * Adjust this players rating based on losing to the opponent.
     *
     * @param opponent The opponent to play
     */
    public void adjustFromLoss(EloHelper opponent) {
        if (countGames) {
            gamesPlayed++;
        }
        this.setRating(this.newRatingLoss(opponent));
    }

    /**
     * A helper method to adjust the scores of a winner and loser.
     *
     * @param winner The player that won
     * @param loser The player that lost
     */
    public static void adjust(EloHelper winner, EloHelper loser) {
        winner.adjustFromWin(loser);
        loser.adjustFromLoss(winner);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.countGames ? 1 : 0);
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.rating) ^ (Double.doubleToLongBits(this.rating) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EloHelper other = (EloHelper) obj;
        if (this.countGames != other.countGames) {
            return false;
        }
        if (Double.doubleToLongBits(this.rating) != Double.doubleToLongBits(other.rating)) {
            return false;
        }
        if (this.gamesPlayed != other.gamesPlayed) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (countGames) {
            return "Elo{" + "rating=" + rating + ", gamesPlayed=" + gamesPlayed + '}';
        }
        return "Elo{" + "rating=" + rating + '}';
    }


}

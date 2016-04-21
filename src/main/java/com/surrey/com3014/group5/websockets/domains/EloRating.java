package com.surrey.com3014.group5.websockets.domains;

/**
 * Elo rating of the user.
 *
 * @author Aung Thu Moe
 */
public interface EloRating {
    /**
     * Get user id.
     *
     * @return user id.
     */
    long getUserId();

    /**
     * Get elo rating.
     *
     * @return elo rating.
     */
    double getRating();

    /**
     * Set elo rating.
     *
     * @param rating elo rating.
     */
    void setRating(double rating);
}

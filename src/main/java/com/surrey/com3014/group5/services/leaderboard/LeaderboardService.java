package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.services.MutableService;
import com.surrey.com3014.group5.websockets.domains.EloRating;

import java.util.List;
import java.util.Optional;

/**
 * Service for manaing leaderboard.
 *
 * @author Spyros Balkonis
 */
public interface LeaderboardService extends MutableService<Leaderboard> {

    /**
     * Find leaderboard by userID
     *
     * @param userID of the associated leaderboard.
     * @return leaderboard associated with the provided userID
     */
    Optional<Leaderboard> findByUserID(long userID);

    /**
     * Get the top 10 leaderboard rating.
     *
     * @return top 10 leaderboard rating.
     */
    List<Leaderboard> findAllByOrderByRatingDescUserAsc();

    /**
     * This method adjust Elo rating of winner and loser
     *
     * @param winner of the game.
     * @param loser  of the game.
     */
    void adjustEloRating(EloRating winner, EloRating loser);
}

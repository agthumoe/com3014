package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.services.MutableService;

import java.util.List;
import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
public interface LeaderboardService extends MutableService<Leaderboard>{


    Optional<Leaderboard> findByUserID(long userID);

    List<Leaderboard> findAllByOrderByRatioDescUserAsc();

    void setWinner(long userID);

    void setLoser(long userID);
}

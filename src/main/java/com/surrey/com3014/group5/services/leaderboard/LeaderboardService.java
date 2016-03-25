package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.MutableService;

import java.util.List;
import java.util.Optional;

/**
 * @author Spyros Balkonis
 */
public interface LeaderboardService extends MutableService<Leaderboard>{


    Optional<Leaderboard> findByUser(User user);

    List<Leaderboard> findAllByOrderByRatioDesc();

}

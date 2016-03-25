package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;

import java.util.List;

/**
 * @author Spyros Balkonis
 */
@org.springframework.stereotype.Repository
public interface LeaderboardRepository extends Repository<Leaderboard>{

    Leaderboard findByUser(User user);

    List<Leaderboard> findAllByOrderByRatioDesc();

}

package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
@org.springframework.stereotype.Repository
public interface LeaderboardRepository extends Repository<Leaderboard>{

    Optional<Leaderboard> findByUser(User user);

    List<Leaderboard> findAllByOrderByRatioDescUserAsc(Pageable pageable);

}

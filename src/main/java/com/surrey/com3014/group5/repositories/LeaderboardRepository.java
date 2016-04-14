package com.surrey.com3014.group5.repositories;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to access persistence {@link Leaderboard} entity.
 *
 * @author Aung Thu Moe
 */
@org.springframework.stereotype.Repository
public interface LeaderboardRepository extends Repository<Leaderboard> {

    /**
     * Retrieves a {@link Leaderboard} by its {@link User}
     *
     * @param user to be searched
     * @return The {@link Leaderboard} of the given {@link User} wrapped in the {@link Optional}
     * or an empty {@link Optional} container if not found.
     */
    Optional<Leaderboard> findByUser(User user);

    /**
     * Retrieves pageable list of {@link Leaderboard} ordered by ratio in descending order,
     * ordered by username in ascending order.
     *
     * @param pageable A Paganation information from the cleint
     * @return List of {@link Leaderboard} satisfying the provided {@link Pageable} information
     */
    List<Leaderboard> findAllByOrderByRatioDescUserUsernameAsc(Pageable pageable);

}

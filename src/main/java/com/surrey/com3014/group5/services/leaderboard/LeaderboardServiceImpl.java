package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.LeaderboardRepository;
import com.surrey.com3014.group5.services.AbstractMutableService;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author Spyros Balkonis
 */
@Service("leaderboardService")
public class LeaderboardServiceImpl extends AbstractMutableService<Leaderboard> implements LeaderboardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardServiceImpl.class);
    @Autowired
    private UserService userService;

    @Autowired
    public LeaderboardServiceImpl(LeaderboardRepository leaderboardRepository) {
        super(leaderboardRepository);
    }

    public LeaderboardRepository getLeaderboardRepository() {
        return (LeaderboardRepository) super.getRepository();
    }

    @Override
    public <S extends Leaderboard> S create(S s) {
        return super.create(s);
    }

    @Override
    public Optional<Leaderboard> findByUserID(long userID) {
        Optional<User> maybeUser = userService.findOne(userID);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException("The requested resource does not exist");
        }
        return this.getLeaderboardRepository().findByUser(maybeUser.get());
    }

    @Override
    public List<Leaderboard> findAllByOrderByRatioDescUserAsc() {
        return this.getLeaderboardRepository().findAllByOrderByRatioDescUserUsernameAsc(new PageRequest(0, 10));
    }

    @Override
    public void setWinner(long userID) {
        Optional<Leaderboard> maybeLeaderboard = findByUserID(userID);
        if (!maybeLeaderboard.isPresent()) {
            throw new ResourceNotFoundException("The requested resource does not exist");
        }
        final Leaderboard leaderboard = maybeLeaderboard.get();
        leaderboard.setWins(leaderboard.getWins() + 1);
        long totalGamePlay = leaderboard.getWins() + leaderboard.getLosses();
        if (totalGamePlay != 0) {
            leaderboard.setRatio(((double) leaderboard.getWins() / totalGamePlay));
            LOGGER.debug("User: {} -> ratio {}", userID, leaderboard.getRatio());
        }
        getLeaderboardRepository().save(leaderboard);
    }

    @Override
    public void setLoser(long userID) {
        Optional<Leaderboard> maybeLeaderboard = findByUserID(userID);
        if (!maybeLeaderboard.isPresent()) {
            throw new ResourceNotFoundException("The requested resource does not exist");
        }
        final Leaderboard leaderboard = maybeLeaderboard.get();
        leaderboard.setLosses(leaderboard.getLosses() + 1);
        long totalGamePlay = leaderboard.getWins() + leaderboard.getLosses();
        if (totalGamePlay != 0) {
            leaderboard.setRatio(((double) leaderboard.getWins() / totalGamePlay));
            LOGGER.debug("User: {} -> ratio {}", userID, leaderboard.getRatio());
        }
        getLeaderboardRepository().save(leaderboard);
    }
}

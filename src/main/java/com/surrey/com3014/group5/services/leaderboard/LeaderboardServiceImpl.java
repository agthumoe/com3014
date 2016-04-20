package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.LeaderboardRepository;
import com.surrey.com3014.group5.services.AbstractMutableService;
import com.surrey.com3014.group5.services.user.UserService;
import com.surrey.com3014.group5.helpers.EloHelper;
import com.surrey.com3014.group5.websockets.domains.EloRating;
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
    public List<Leaderboard> findAllByOrderByRatingDescUserAsc() {
        return this.getLeaderboardRepository().findAllByOrderByRatingDescUserUsernameAsc(new PageRequest(0, 10));
    }

    @Override
    public void adjustEloRating(EloRating winner, EloRating loser) {

        Optional<Leaderboard> maybeWinnerLeaderboard = findByUserID(winner.getUserId());
        if (!maybeWinnerLeaderboard.isPresent()) {
            throw new ResourceNotFoundException("The requested resource does not exist");
        }

        Optional<Leaderboard> maybeLoserLeaderboard = findByUserID(loser.getUserId());
        if (!maybeLoserLeaderboard.isPresent()) {
            throw new ResourceNotFoundException("The requested resource does not exist");
        }

        final Leaderboard leaderboardWinner = maybeWinnerLeaderboard.get();

        final Leaderboard leaderboardLoser = maybeLoserLeaderboard.get();

        final EloHelper winnerRating = new EloHelper(leaderboardWinner.getRating());
        final EloHelper loserRating = new EloHelper(leaderboardLoser.getRating());

        EloHelper.adjust(winnerRating, loserRating);

        // update the rating of winner and loser
        winner.setRating(winnerRating.getRating());
        loser.setRating(loserRating.getRating());

        // update the leaderboard rating in the database.
        leaderboardWinner.setRating(winnerRating.getRating());
        leaderboardLoser.setRating(loserRating.getRating());

        // update the leaderboard win and lose count in the database
        leaderboardWinner.setWins(leaderboardWinner.getWins() + 1);
        leaderboardLoser.setLosses(leaderboardLoser.getLosses() + 1);

        // save the results
        getLeaderboardRepository().save(leaderboardWinner);
        getLeaderboardRepository().save(leaderboardLoser);
    }
}

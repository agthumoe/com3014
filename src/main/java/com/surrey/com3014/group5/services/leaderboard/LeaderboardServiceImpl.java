package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.LeaderboardRepository;
import com.surrey.com3014.group5.services.AbstractMutableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author Spyros Balkonis
 */
@Service("leaderboardService")
public class LeaderboardServiceImpl extends AbstractMutableService<Leaderboard> implements LeaderboardService{

    private final Logger LOGGER = LoggerFactory.getLogger(LeaderboardServiceImpl.class);

    @Autowired
    public LeaderboardServiceImpl(LeaderboardRepository leaderboardRepository){
        super(leaderboardRepository);
    }

    public LeaderboardRepository getLeaderboardRepository() {
        return (LeaderboardRepository) super.getRepository();
    }

    @Override
    public <S extends Leaderboard> S create(S s){
        return super.create(s);
    }

    @Override
    public Optional<Leaderboard> findByUser(User user) {
        return this.getLeaderboardRepository().findByUser(user);
    }

    @Override
    public List<Leaderboard> findAllByOrderByRatioDesc() {
        return this.getLeaderboardRepository().findAllByOrderByRatioDesc();
    }
}

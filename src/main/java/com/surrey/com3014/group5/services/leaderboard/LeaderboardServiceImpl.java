package com.surrey.com3014.group5.services.leaderboard;

import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.LeaderboardRepository;
import com.surrey.com3014.group5.services.AbstractMutableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author Spyros Balkonis
 */
@Service("leaderboardService")
public class LeaderboardServiceImpl extends AbstractMutableService<Leaderboard> implements LeaderboardService{

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
    public List<Leaderboard> findAllByOrderByRatioDescUserAsc() {
        return this.getLeaderboardRepository().findAllByOrderByRatioDescUserUsernameAsc(new PageRequest(0, 10));
    }
}

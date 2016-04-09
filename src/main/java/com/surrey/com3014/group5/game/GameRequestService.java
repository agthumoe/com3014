package com.surrey.com3014.group5.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.surrey.com3014.group5.models.impl.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Aung Thu Moe
 */
@Service
public class GameRequestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRequestService.class);
    private LoadingCache<String, GameRequest> gameRequests = CacheBuilder.newBuilder().build(new CacheLoader<String, GameRequest>() {
        @Override
        public GameRequest load(String gameID) throws Exception {
            return new GameRequest();
        }
    });

    public GameRequest registerGameRequest(String gameID, User challenger, User challenged) {
        Assert.notNull(gameID);
        Assert.notNull(challenger);
        Assert.notNull(challenged);
        final GameRequest gameRequest = this.gameRequests.getUnchecked(gameID);
        gameRequest.setGameID(gameID);
        gameRequest.setChallenger(challenger);
        gameRequest.setChallenged(challenged);
        LOGGER.debug("new game request: " + gameRequest.toString());
        return gameRequest;
    }

    public GameRequest getGameRequest(String gameID) {
        return this.gameRequests.getUnchecked(gameID);
    }
}

package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.domains.GameRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Aung Thu Moe
 */
@Service
public class GameRequestService extends WebsocketService<String, GameRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRequestService.class);

    @Override
    protected GameRequest init() {
        return new GameRequest();
    }

    public GameRequest registerGameRequest(String gameID, User challenger, User challenged) {
        Assert.notNull(gameID);
        Assert.notNull(challenger);
        Assert.notNull(challenged);
        final GameRequest gameRequest = this.cache.getUnchecked(gameID);
        gameRequest.setGameID(gameID);
        gameRequest.setChallenger(challenger);
        gameRequest.setChallenged(challenged);
        LOGGER.debug("new game request: " + gameRequest.toString());
        return gameRequest;
    }

    public GameRequest getGameRequest(String gameID) {
        return this.cache.getUnchecked(gameID);
    }
}

package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.domains.GameRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Game request service stores all game request information in the cache.
 *
 * @author Aung Thu Moe
 */
@Service
public class GameRequestService extends WebsocketService<String, GameRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRequestService.class);

    /**
     * Initialise a new game request object.
     *
     * @return a new game request object.
     */
    @Override
    protected GameRequest init() {
        return new GameRequest();
    }

    /**
     * This method register a new game request in the cache.
     *
     * @param gameID     of the new game to be registered.
     * @param challenger of this game.
     * @param challenged of this game.
     * @return a new game request registered with the provided information.
     */
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

    /**
     * Query the gameRequest from the cache providing the gameID.
     *
     * @param gameID of a game request.
     * @return gameRequest of the provided gameID.
     */
    public GameRequest getGameRequest(String gameID) {
        return this.cache.getUnchecked(gameID);
    }
}

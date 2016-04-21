package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.websockets.domains.Game;
import com.surrey.com3014.group5.websockets.domains.GameRequest;
import com.surrey.com3014.group5.websockets.dto.PlayerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * This service stores informations regarding the game in the cache.
 *
 * @author Aung Thu Moe
 */
@Service
public class GameService extends WebsocketService<String, Game> {

    /**
     * Initialise a new Game object.
     *
     * @return a new Game object.
     */
    @Override
    protected Game init() {
        return new Game();
    }

    /**
     * Register a new game by providing a gameRequest
     *
     * @param gameRequest to be registered as a new game.
     * @return a new game
     */
    public Game registerNewGame(GameRequest gameRequest) {
        Assert.notNull(gameRequest);
        final Game game = this.cache.getUnchecked(gameRequest.getGameID());
        game.setGameID(gameRequest.getGameID());
        game.setChallenged(new PlayerDTO(gameRequest.getChallenged(), PlayerDTO.CHALLENGED));
        game.setChallenger(new PlayerDTO(gameRequest.getChallenger(), PlayerDTO.CHALLENGER));
        return game;
    }

    /**
     * Get the existing game stored in the cache.
     *
     * @param gameID of the game
     * @return game of the provided gameID
     */
    public Game getGame(String gameID) {
        return this.cache.getUnchecked(gameID);
    }
}

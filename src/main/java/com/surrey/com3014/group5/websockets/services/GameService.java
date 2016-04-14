package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.websockets.dto.PlayerDTO;
import com.surrey.com3014.group5.websockets.domains.Game;
import com.surrey.com3014.group5.websockets.domains.GameRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Aung Thu Moe
 */
@Service
public class GameService extends WebsocketService<String, Game> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    @Override
    protected Game init() {
        return new Game();
    }

    public Game registerNewGame(GameRequest gameRequest) {
        Assert.notNull(gameRequest);
        final Game game = this.cache.getUnchecked(gameRequest.getGameID());
        game.setGameID(gameRequest.getGameID());
        game.setChallenged(new PlayerDTO(gameRequest.getChallenged(), PlayerDTO.CHALLENGED));
        game.setChallenger(new PlayerDTO(gameRequest.getChallenger(), PlayerDTO.CHALLENGER));
        return game;
    }

    public Game getGame(String gameID) {
        return this.cache.getUnchecked(gameID);
    }
}

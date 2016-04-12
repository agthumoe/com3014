package com.surrey.com3014.group5.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.surrey.com3014.group5.dto.users.GamerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Aung Thu Moe
 */
@Service
public class GameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private LoadingCache<String, Game> games = CacheBuilder.newBuilder().build(new CacheLoader<String, Game>() {
        @Override
        public Game load(String key) throws Exception {
            return new Game();
        }
    });

    public Game registerNewGame(GameRequest gameRequest) {
        Assert.notNull(gameRequest);
        final Game game = this.games.getUnchecked(gameRequest.getGameID());
        game.setGameID(gameRequest.getGameID());
        game.setChallenged(new GamerDTO(gameRequest.getChallenged(), GamerDTO.CHALLENGED));
        game.setChallenger(new GamerDTO(gameRequest.getChallenger(), GamerDTO.CHALLENGER));
        return game;
    }

    public Game getGame(String gameID) {
        return this.games.getUnchecked(gameID);
    }
}

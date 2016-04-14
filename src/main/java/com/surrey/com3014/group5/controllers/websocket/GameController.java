package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.dto.users.GamerDTO;
import com.surrey.com3014.group5.game.Command;
import com.surrey.com3014.group5.game.Game;
import com.surrey.com3014.group5.game.GameService;
import com.surrey.com3014.group5.game.Resolution;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * @author Aung Thu Moe
 */
@Controller
public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private static final String OUT_BOUND = "/topic/game";
    private static final String IN_BOUND = "/queue/game";
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GameService gameService;

    @Autowired
    private LeaderboardService leaderboardService;

    @MessageMapping(IN_BOUND)
    public void request(String message, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        Command command = new Command(message);
        final Game game = gameService.getGame(command.getStringData("gameID"));
        if (this.validate(user, game, command)) {
            if (Command.Game.LOADED.equals(command.getCommand())) {
                loadedAndPing(user, game, command);
            } else if (Command.Game.PONG.equals(command.getCommand())) {
                pongAndPrepare(user, game, command);
            } else if (Command.Game.READY.equals(command.getCommand())) {
                readyAndStart(user, game, command);
            } else if (Command.Game.UPDATE.equals(command.getCommand())) {
                update(user, game, command);
            } // any other commands will be ignore
        }
    }

    private boolean validate(User user, Game game, Command command) {
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
        if (game.getGameID() == null) {
            response.put("command", Command.Error.DENY);
            template.convertAndSendToUser(user.getUsername(), OUT_BOUND, response.toString());
            return false;
        } else if (game.isExpired()) {
            response.put("command", Command.Error.EXPIRED);
            template.convertAndSendToUser(user.getUsername(), OUT_BOUND, response.toString());
            return false;
        }
        return true;
    }

    private void loadedAndPing(final User user, final Game game, final Command command) {
        // record gamer's resolutions
        LOGGER.debug("User: {} -> loaded and ping", user.getUsername());
        game.setGamerResolution(user.getId(), command.getIntegerData("height"), command.getIntegerData("width"));
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
        response.put("command", Command.Game.PING);
        // return the ping message
        template.convertAndSendToUser(game.getChallenger().getUsername(), OUT_BOUND, response.toString());
        game.getChallenger().setMessageSentTime(System.currentTimeMillis());
        template.convertAndSendToUser(game.getChallenged().getUsername(), OUT_BOUND, response.toString());
        game.getChallenged().setMessageSentTime(System.currentTimeMillis());
    }

    private void pongAndPrepare(User user, Game game, Command command) {
        LOGGER.debug("User: {} -> pong and prepare", user.getUsername());
        // calculate the transmission delay
        game.getChallenger().setMessageReceivedTime(System.currentTimeMillis());
        game.getChallenged().setMessageReceivedTime(System.currentTimeMillis());
        // send prepare message with recommanded resolutions for both players.
        final Resolution resolution = game.getResolution();
        if (resolution != null) {
            resolution.setHeight(resolution.getHeight() - Resolution.OFFSET);
            resolution.setWidth(resolution.getWidth() - Resolution.OFFSET);
            final JSONObject responseForChallenger = new JSONObject();
            responseForChallenger.put("gameID", game.getGameID());
            responseForChallenger.put("role", game.getChallenger().getRole());
            responseForChallenger.put("height", resolution.getHeight());
            responseForChallenger.put("width", resolution.getWidth());
            responseForChallenger.put("command", Command.Game.PREP);

            JSONObject responseForChallenged = new JSONObject();
            responseForChallenged.put("gameID", game.getGameID());
            responseForChallenged.put("role", game.getChallenged().getRole());
            responseForChallenged.put("height", resolution.getHeight());
            responseForChallenged.put("width", resolution.getWidth());
            responseForChallenged.put("command", Command.Game.PREP);

            template.convertAndSendToUser(game.getChallenger().getUsername(), OUT_BOUND, responseForChallenger.toString());
            game.getChallenger().setMessageSentTime(System.currentTimeMillis());
            template.convertAndSendToUser(game.getChallenged().getUsername(), OUT_BOUND, responseForChallenged.toString());
            game.getChallenged().setMessageSentTime(System.currentTimeMillis());
        }

    }

    private void readyAndStart(User user, Game game, Command command) {
        LOGGER.debug("User: {} -> ready", user.getUsername());
        GamerDTO currentPlayer = game.getCurrentPlayer(user.getId());
        currentPlayer.setReady(true);
        currentPlayer.setMessageReceivedTime(System.currentTimeMillis());

        // only response when both players are ready
        if (game.getChallenged().isReady() && game.getChallenger().isReady()) {
            final JSONObject response = new JSONObject();
            response.put("gameID", game.getGameID());
            response.put("command", Command.Game.START);
            // start game in time to start - transmission delay
            response.put("start_in", Game.TIME_TO_START - currentPlayer.getPingRate());
            template.convertAndSendToUser(game.getChallenger().getUsername(), OUT_BOUND, response.toString());
            LOGGER.debug("Challenger: {} -> start", game.getChallenger().getUsername());
            template.convertAndSendToUser(game.getChallenged().getUsername(), OUT_BOUND, response.toString());
            LOGGER.debug("Challenged: {} -> start", game.getChallenged().getUsername());
        }
    }

    private void update(final User user, final Game game, final Command command) {
        GamerDTO oppositePlayer = game.getOppositePlayer(user.getId());
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
        response.put("vx", command.getDoubleData("vx"));
        response.put("vy", command.getDoubleData("vy"));
        response.put("magnitude", command.getIntegerData("magnitude"));
        response.put("rotation", command.getDoubleData("rotation"));
        String status = command.getStringData("status");
        response.put("status", status);
        response.put("command", Command.Game.UPDATE);
        template.convertAndSendToUser(oppositePlayer.getUsername(), OUT_BOUND, response.toString());
        if (status != null && status.equals("EXPLODED")) {
            game.setExpired(true);
            leaderboardService.setLoser(user.getId());
            leaderboardService.setWinner(game.getOppositePlayer(user.getId()).getId());
            LOGGER.debug("game: {}, has finished!", game.getGameID());
        }
    }
}

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
import java.util.Optional;

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
        try {
            final Game game = gameService.getGame(command.getStringData("gameID"));
            if (this.validate(user, game, command)) {
                if (Command.Game.LOADED.equals(command.getCommand())) {
                    LOGGER.debug("user: {}, Server receive <- GAME.LOADED", user.getUsername());
                    loadedAndPing(user, game, command);
                } else if (Command.Game.PONG.equals(command.getCommand())) {
                    LOGGER.debug("user: {}, Server receive <- GAME.PONG", user.getUsername());
                    pongAndPrepare(user, game);
                } else if (Command.Game.READY.equals(command.getCommand())) {
                    LOGGER.debug("user: {}, Server receive <- GAME.READY", user.getUsername());
                    readyAndStart(user, game);
                } else if (Command.Game.UPDATE.equals(command.getCommand())) {
                    update(user, game, command);
                } // any other commands will be ignore
            }
        } catch (Exception e) {
            LOGGER.error("user: {}, error: {}, request: {}", user.getUsername(), e.getMessage(), message);
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
        LOGGER.debug("User: {}, Server response -> GAME.PING", user.getUsername());
        game.setGamerResolution(user.getId(), command.getIntegerData("height"), command.getIntegerData("width"));
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
        response.put("command", Command.Game.PING);
        // return the ping message
        template.convertAndSendToUser(user.getUsername(), OUT_BOUND, response.toString());
        game.getCurrentPlayer(user.getId()).setMessageSentTime(System.currentTimeMillis());
    }

    private void pongAndPrepare(User user, Game game) {
        LOGGER.debug("User: {}, Server response -> GAME.PREPARE", user.getUsername());
        // calculate the transmission delay
        game.getChallenger().setMessageReceivedTime(System.currentTimeMillis());
        game.getChallenged().setMessageReceivedTime(System.currentTimeMillis());
        // send prepare message with recommanded resolutions for both players.
        final Optional<Resolution> resolution = game.getResolution();
        if (resolution.isPresent()) {
            Resolution recommened = resolution.get();
            recommened.setHeight(recommened.getHeight() - Resolution.OFFSET);
            recommened.setWidth(recommened.getWidth() - Resolution.OFFSET);
            final JSONObject responseForChallenger = new JSONObject();
            responseForChallenger.put("gameID", game.getGameID());
            responseForChallenger.put("role", game.getChallenger().getRole());
            responseForChallenger.put("height", recommened.getHeight());
            responseForChallenger.put("width", recommened.getWidth());
            responseForChallenger.put("command", Command.Game.PREP);

            JSONObject responseForChallenged = new JSONObject();
            responseForChallenged.put("gameID", game.getGameID());
            responseForChallenged.put("role", game.getChallenged().getRole());
            responseForChallenged.put("height", recommened.getHeight());
            responseForChallenged.put("width", recommened.getWidth());
            responseForChallenged.put("command", Command.Game.PREP);

            template.convertAndSendToUser(game.getChallenger().getUsername(), OUT_BOUND, responseForChallenger.toString());
            game.getChallenger().setMessageSentTime(System.currentTimeMillis());
            template.convertAndSendToUser(game.getChallenged().getUsername(), OUT_BOUND, responseForChallenged.toString());
            game.getChallenged().setMessageSentTime(System.currentTimeMillis());
        }
    }

    private void readyAndStart(User user, Game game) {
        GamerDTO currentPlayer = game.getCurrentPlayer(user.getId());
        currentPlayer.setReady(true);
        currentPlayer.setMessageReceivedTime(System.currentTimeMillis());
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
        // if game is already started, response with started message
        if (game.isStarted()) {
            LOGGER.debug("User: {}, Server response -> GAME.STARTED", user.getUsername());
            response.put("command", Command.Game.STARTED);
            template.convertAndSendToUser(user.getUsername(), OUT_BOUND, response.toString());
        } else if (game.getChallenged().isReady() && game.getChallenger().isReady()) {
            // only response when both players are ready
            response.put("command", Command.Game.START);
            // start game in time to start - transmission delay
            response.put("start_in", Game.TIME_TO_START - currentPlayer.getPingRate());
            template.convertAndSendToUser(game.getChallenger().getUsername(), OUT_BOUND, response.toString());
            LOGGER.debug("Challenger: {}, Server response -> GAME.START", game.getChallenger().getUsername());
            template.convertAndSendToUser(game.getChallenged().getUsername(), OUT_BOUND, response.toString());
            LOGGER.debug("Challenged: {}, Server response -> GAME.START", game.getChallenged().getUsername());
            game.setStarted(true);
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

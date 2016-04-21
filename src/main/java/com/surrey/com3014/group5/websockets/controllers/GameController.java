package com.surrey.com3014.group5.websockets.controllers;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import com.surrey.com3014.group5.websockets.domains.Command;
import com.surrey.com3014.group5.websockets.domains.Game;
import com.surrey.com3014.group5.websockets.domains.Resolution;
import com.surrey.com3014.group5.websockets.dto.PlayerDTO;
import com.surrey.com3014.group5.websockets.services.ActiveUserService;
import com.surrey.com3014.group5.websockets.services.GameService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

/**
 * This class is the main game websocket protocol. Handle all the messages regarding the game playing information.
 *
 * @author Aung Thu Moe
 */
@Controller
public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    /**
     * Outgoing websocket address.
     */
    private static final String OUT_BOUND = "/topic/game";
    /**
     * Incoming websocket address
     */
    private static final String IN_BOUND = "/queue/game";

    /**
     * This messaging template provides a method to send message to a specific users or all users.
     */
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * GameService to access all game information stored in the cache.
     */
    @Autowired
    private GameService gameService;

    /**
     * LeaderboardService to update leaderboard information after the game has finished.
     */
    @Autowired
    private LeaderboardService leaderboardService;

    /**
     * ActiveUserService to update the rating of the active users stored in the cache.
     */
    @Autowired
    private ActiveUserService activeUserService;

    /**
     * Handle the incoming the game message.
     *
     * @param message   the incoming message.
     * @param principal current user principal.
     */
    @MessageMapping(IN_BOUND)
    public void request(String message, Principal principal) {
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

    /**
     * Validate if the gameId is exist or if the game is expired.
     *
     * @param user    current user principal.
     * @param game    current playing game.
     * @param command current game command.
     * @return true if the game id exist and game is not yet expired, false otherwise.
     */
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

    /**
     * If the clients response with LOAD message, the server response with PING message.
     *
     * @param user    current user principal.
     * @param game    current playing game.
     * @param command current game command.
     */
    private void loadedAndPing(final User user, final Game game, final Command command) {
        // record player's resolutions
        LOGGER.debug("User: {}, Server response -> GAME.PING", user.getUsername());
        game.setPlayerResolution(user.getId(), command.getIntegerData("height"), command.getIntegerData("width"));
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
        response.put("command", Command.Game.PING);
        // return the ping message
        template.convertAndSendToUser(user.getUsername(), OUT_BOUND, response.toString());
        game.getCurrentPlayer(user.getId()).setMessageSentTime(System.currentTimeMillis());
    }

    /**
     * If the clients response with PONG message, server responses with PREPARE message with the recommended screen
     * resolution for both palyers. Recommended resolution is only sent when both players has been provided their
     * screen resolution. This method also calculates the transmission delays.
     *
     * @param user current user principal.
     * @param game current playing game.
     */
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

    /**
     * If both clients response that they are ready, the server replies with START message.
     *
     * @param user current user principal.
     * @param game current playing game.
     */
    private void readyAndStart(User user, Game game) {
        PlayerDTO currentPlayer = game.getCurrentPlayer(user.getId());
        currentPlayer.setReady(true);
        currentPlayer.setMessageReceivedTime(System.currentTimeMillis());
        final JSONObject response = new JSONObject();
        response.put("gameID", game.getGameID());
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

    /**
     * This method just redirects the message from one player to the other player in the same game.
     *
     * @param user    current user principal.
     * @param game    current playing game.
     * @param command current game command.
     */
    private void update(final User user, final Game game, final Command command) {
        final PlayerDTO currentPlayer = game.getCurrentPlayer(user.getId());
        final PlayerDTO oppositePlayer = game.getOppositePlayer(user.getId());
        final JSONObject response = command.getData();
        response.put("command", Command.Game.UPDATE);
        template.convertAndSendToUser(oppositePlayer.getUsername(), OUT_BOUND, response.toString());
        String status = command.getStringData("status");
        // if one player sent with EXPLODED message, the opposite player was won and update the leaderboard,
        // activeUsers cache and end the game.
        if (status != null && status.equals("EXPLODED")) {
            game.setExpired(true);
            // adjust and update the elo rating in the database.
            leaderboardService.adjustEloRating(oppositePlayer, currentPlayer);
            // update the rating in the active user lists.
            activeUserService.updateUserRating(oppositePlayer);
            activeUserService.updateUserRating(currentPlayer);
            LOGGER.debug("game: {}, has finished!", game.getGameID());
        }
    }
}

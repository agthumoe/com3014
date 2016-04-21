package com.surrey.com3014.group5.websockets.controllers;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import com.surrey.com3014.group5.websockets.domains.Command;
import com.surrey.com3014.group5.websockets.domains.GameRequest;
import com.surrey.com3014.group5.websockets.services.GameRequestService;
import com.surrey.com3014.group5.websockets.services.GameService;
import com.surrey.com3014.group5.websockets.utils.RandomUtils;
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
 * Websocket controller to handle new game challenge
 *
 * @author Aung Thu Moe
 */
@Controller
public class GameChallengeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameChallengeController.class);

    /**
     * This messaging template provides a method to send message to a specific users or all users.
     */
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Userservice to access user dao.
     */
    @Autowired
    private UserService userService;

    /**
     * GameRequestService to access all game request stored in the cache.
     */
    @Autowired
    private GameRequestService gameRequestService;

    /**
     * GameService to access all game information stored in the cache.
     */
    @Autowired
    private GameService gameService;

    /**
     * Handle all commands sent to <code>/queue/challenge</code> channel.
     *
     * @param message   A websocket command message.
     * @param principal current user principal.
     */
    @MessageMapping("/queue/challenge")
    public void handleCommand(String message, Principal principal) {
        User challenger = (User) ((Authentication) principal).getPrincipal();
        Command command = new Command(message);
        LOGGER.debug(command.toString());
        if (Command.Challenge.NEW.equals(command.getCommand())) {
            Optional<User> optional = userService.findOne(command.getIntegerData("userID"));
            if (!optional.isPresent()) {
                throw new ResourceNotFoundException("not found");
            }
            User challenged = optional.get();
            newChallenge(challenger, challenged);
        } else if (Command.Challenge.DECLINE.equals(command.getCommand())) {
            denyChallenge(command.getStringData("gameID"));
        } else if (Command.Challenge.ACCEPT.equals(command.getCommand())) {
            acceptChallenge(command.getStringData("gameID"));
        }
    }

    /**
     * Handle new game challenge command.
     *
     * @param challenger of new game.
     * @param challenged of new game.
     */
    private void newChallenge(User challenger, User challenged) {
        final GameRequest gameRequest = this.gameRequestService.registerGameRequest(RandomUtils.getRandom(), challenger, challenged);
        JSONObject response = new JSONObject();
        response.put("gameID", gameRequest.getGameID());
        response.put("command", Command.Challenge.NEW);
        JSONObject challengerJSON = new JSONObject();
        challengerJSON.put("name", gameRequest.getChallenger().getName());
        response.put("challenger", challengerJSON);
        template.convertAndSendToUser(challenged.getUsername(), "/topic/challenge", response.toString());
        LOGGER.debug("new challenge: " + gameRequest.toString());
    }

    /**
     * Handle challenge deny command.
     *
     * @param gameID of the new game.
     */
    private void denyChallenge(String gameID) {
        final GameRequest gameRequest = this.gameRequestService.getGameRequest(gameID);
        if (gameRequest.isExpired()) {
            sendExpiredChallengeMessage(gameRequest);
        } else {
            JSONObject response = new JSONObject();
            response.put("gameID", gameRequest.getGameID());
            JSONObject challengedJSON = new JSONObject();
            challengedJSON.put("name", gameRequest.getChallenged().getName());
            response.put("challenged", challengedJSON);
            response.put("command", Command.Challenge.DECLINE);
            template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/challenge", response.toString());
            LOGGER.debug("deny challenge: " + gameRequest.toString());
        }
    }

    /**
     * Handle challenge accept command.
     *
     * @param gameID of the new game.
     */
    private void acceptChallenge(String gameID) {
        final GameRequest gameRequest = this.gameRequestService.getGameRequest(gameID);
        if (gameRequest.isExpired()) {
            sendExpiredChallengeMessage(gameRequest);
        } else {
            JSONObject response = new JSONObject();
            response.put("gameID", gameRequest.getGameID());
            response.put("command", Command.Challenge.ACCEPT);
            JSONObject challengedJSON = new JSONObject();
            challengedJSON.put("name", gameRequest.getChallenged().getName());
            response.put("challenged", challengedJSON);
            template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/challenge", response.toString());
            // register new game in the game service
            this.gameService.registerNewGame(gameRequest);
            LOGGER.debug("accept challenge: " + gameRequest.toString());
        }
    }

    /**
     * Handle if the challenge is expired or not. Send message to user if the game was expired.
     *
     * @param gameRequest GameRequest sent by a player.
     */
    private void sendExpiredChallengeMessage(GameRequest gameRequest) {
        LOGGER.debug("send expiration message");
        JSONObject response = new JSONObject();
        response.put("gameID", gameRequest.getGameID());
        response.put("command", Command.Challenge.TIMEOUT);
        JSONObject challengerJSON = new JSONObject();
        challengerJSON.put("name", gameRequest.getChallenger().getName());
        response.put("challenger", challengerJSON);
        JSONObject challengedJSON = new JSONObject();
        challengedJSON.put("name", gameRequest.getChallenged().getName());
        response.put("challenged", challengedJSON);
        template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/challenge", response.toString());
        template.convertAndSendToUser(gameRequest.getChallenged().getUsername(), "/topic/challenge", response.toString());
    }
}

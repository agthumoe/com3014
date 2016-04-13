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
    private static final long TIME_TO_START = 5000;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GameService gameService;

    @Autowired
    private LeaderboardService leaderboardService;

    @MessageMapping("/queue/game")
    public void request(String message, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        Command command = new Command(message);
        final Game game = gameService.getGame(command.getStringData("gameID"));
        if (game.getGameID() == null) {
            stompHeaderAccessor.setMessage("You are not allow to subscribe this socket");
            template.convertAndSendToUser(user.getUsername(),"/topic/game", "{\"status\": \"error\"}", stompHeaderAccessor.getMessageHeaders());
        } else if (game.isExpired()) {
            stompHeaderAccessor.setMessage("Game is expired");
            template.convertAndSendToUser(user.getUsername(),"/topic/game", "{\"status\": \"error\"}", stompHeaderAccessor.getMessageHeaders());
        } else {
            if (Command.READY.equals(command.getCommand())) {
                if (game.getGameID() == null) {
                    stompHeaderAccessor.setMessage("You are not allow to subscribe this socket");
                } else if (game.isExpired()) {
                    stompHeaderAccessor.setMessage("Game is expired");
                } else {
                    game.setGamerResolution(user.getId(), command.getIntegerData("height"), command.getIntegerData("width"));
                    response(game);
                }
            } else if (Command.UPDATE.equals(command.getCommand())) {
                GamerDTO oppositePlayer = game.getOppositePlayer(user.getId());
                final JSONObject response = new JSONObject();
                response.put("gameID", game.getGameID());
                response.put("vx", command.getDoubleData("vx"));
                response.put("vy", command.getDoubleData("vy"));
                response.put("magnitude", command.getIntegerData("magnitude"));
                response.put("rotation", command.getDoubleData("rotation"));
                String status = command.getStringData("status");
                response.put("status", status);
                response.put("command", Command.UPDATE);
                template.convertAndSendToUser(oppositePlayer.getUsername(), "/topic/game", response.toString());
                if (status != null && status.equals("EXPLODED")) {
                    game.setExpired(true);
                    leaderboardService.setLoser(user.getId());
                    leaderboardService.setWinner(game.getOppositePlayer(user.getId()).getId());
                    LOGGER.debug("game: {}, has finished!", game.getGameID());
                }
            } else if (Command.PING.equals(command.getCommand())) {
                GamerDTO currentPlayer = game.getCurrentPlayer(user.getId());
                currentPlayer.setMessageReceivedTime(System.currentTimeMillis());
                final JSONObject response = new JSONObject();
                response.put("gameID", game.getGameID());
                response.put("command", Command.START);
                // start game in time to start - transmission delay
                response.put("start_in", TIME_TO_START - currentPlayer.getPingRate());
                template.convertAndSendToUser(currentPlayer.getUsername(), "/topic/game", response.toString());
            }
        }
    }

    public void response(Game game) {
        Resolution resolution = game.getResolution();
        if (resolution != null) {
            resolution.setHeight(resolution.getHeight() - 40);
            resolution.setWidth(resolution.getWidth() - 40);
            final JSONObject responseForChallenger = new JSONObject();
            responseForChallenger.put("gameID", game.getGameID());
            responseForChallenger.put("role", game.getChallenger().getRole());
            responseForChallenger.put("height", resolution.getHeight());
            responseForChallenger.put("width", resolution.getWidth());
            responseForChallenger.put("command", Command.PREP);

            JSONObject responseForChallenged = new JSONObject();
            responseForChallenged.put("gameID", game.getGameID());
            responseForChallenged.put("role", game.getChallenged().getRole());
            responseForChallenged.put("height", resolution.getHeight());
            responseForChallenged.put("width", resolution.getWidth());
            responseForChallenged.put("command", Command.PREP);

            template.convertAndSendToUser(game.getChallenger().getUsername(), "/topic/game", responseForChallenger.toString());
            game.getChallenger().setMessageSentTime(System.currentTimeMillis());
            template.convertAndSendToUser(game.getChallenged().getUsername(), "/topic/game", responseForChallenged.toString());
            game.getChallenged().setMessageSentTime(System.currentTimeMillis());
        }
    }
}

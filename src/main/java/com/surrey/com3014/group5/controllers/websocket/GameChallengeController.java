package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.game.Command;
import com.surrey.com3014.group5.game.GameRequest;
import com.surrey.com3014.group5.game.GameRequestService;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.RandomUtils;
import com.surrey.com3014.group5.services.user.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
@Controller
public class GameChallengeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameChallengeController.class);

    @Autowired
    private TaskScheduler scheduler;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private UserService userService;

    @Autowired
    private GameRequestService gameRequestService;

    @MessageMapping("/queue/game/challenge")
    public void handleCommand(String message, Principal principal) {
        User challenger = (User) ((Authentication) principal).getPrincipal();
        Command command = new Command(message);
        LOGGER.debug(command.toString());
        if (Command.NEW.equals(command.getCommand())) {
            Optional<User> optional = userService.findOne(command.getIntegerData("userID"));
            if (!optional.isPresent()) {
                throw new ResourceNotFoundException("not found");
            }
            User challenged = optional.get();
            newChallenge(challenger, challenged);
        } else if (Command.DENY.equals(command.getCommand())) {
            denyChallenge(command.getStringData("gameID"));
        } else if (Command.ACCEPT.equals(command.getCommand())) {
            acceptChallenge(command.getStringData("gameID"));
        }
    }

    private void newChallenge(User challenger, User challenged) {
        final GameRequest gameRequest = this.gameRequestService.registerGameRequest(RandomUtils.getRandom(), challenger, challenged);
        JSONObject response = new JSONObject();
        response.put("gameID", gameRequest.getGameID());
        response.put("command", Command.NEW);
        JSONObject challengerJSON = new JSONObject();
        challengerJSON.put("name", gameRequest.getChallenger().getName());
        response.put("challenger", challengerJSON);
        template.convertAndSendToUser(challenged.getUsername(), "/topic/game/challenge", response.toString());
        LOGGER.debug("new challenge: " + gameRequest.toString());
    }

    private void denyChallenge(String gameID) {
        final GameRequest gameRequest = this.gameRequestService.getGameRequest(gameID);
        JSONObject response = new JSONObject();
        response.put("gameID", gameRequest.getGameID());
        response.put("command", Command.DENY);
        JSONObject challengedJSON = new JSONObject();
        challengedJSON.put("name", gameRequest.getChallenged().getName());
        response.put("challenged", challengedJSON);
        template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/game/challenge", response.toString());
        LOGGER.debug("deny challenge: " + gameRequest.toString());
    }

    private void acceptChallenge(String gameID) {
        final GameRequest gameRequest = this.gameRequestService.getGameRequest(gameID);
        JSONObject response = new JSONObject();
        response.put("gameID", gameRequest.getGameID());
        response.put("command", Command.ACCEPT);
        JSONObject challengedJSON = new JSONObject();
        challengedJSON.put("name", gameRequest.getChallenged().getName());
        response.put("challenged", challengedJSON);
        template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/game/challenge", response.toString());
        LOGGER.debug("accept challenge: " + gameRequest.toString());
    }
}

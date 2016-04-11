package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.game.Command;
import com.surrey.com3014.group5.game.GameRequest;
import com.surrey.com3014.group5.game.GameRequestService;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.RandomUtils;
import com.surrey.com3014.group5.services.user.UserService;
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

        switch (command.getCommand()) {
            case Command.NEW:
                Optional<User> optional = userService.findOne(command.getIntegerData("userID"));
                if (!optional.isPresent()) {
                    throw new ResourceNotFoundException("not found");
                }
                User challenged = optional.get();
                newChallenge(challenger, challenged);
                break;
            case Command.DENY:
                denyChallenge(command.getStringData("gameID"));
                break;
            case Command.ACCEPT:
                acceptChallenge(command.getStringData("gameID"));
                break;
        }
        LOGGER.debug(command.getCommand());
        LOGGER.debug("userID {}" , command.getIntegerData("userID"));
    }

    private void newChallenge(User challenger, User challenged) {
        final GameRequest gameRequest = this.gameRequestService.registerGameRequest(RandomUtils.getRandom(), challenger, challenged);
        template.convertAndSendToUser(challenged.getUsername(), "/topic/game/challenge", gameRequest);
        LOGGER.debug("new challenge: " + gameRequest.toString());
    }

    private void denyChallenge(String gameID) {
        final GameRequest gameRequest = this.gameRequestService.getGameRequest(gameID);
        template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/game/challenge", "{\"accept\": false}");
        LOGGER.debug("deny challenge: " + gameRequest.toString());
    }

    private void acceptChallenge(String gameID) {
        final GameRequest gameRequest = this.gameRequestService.getGameRequest(gameID);
        template.convertAndSendToUser(gameRequest.getChallenger().getUsername(), "/topic/game/challenge", "{\"accept\": true}");
        LOGGER.debug("accept challenge: " + gameRequest.toString());
    }
}
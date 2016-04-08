package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.game.Command;
import com.surrey.com3014.group5.models.impl.User;
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

    private void receiveChallengeRequest(User from, User to) {
        scheduler.scheduleAtFixedRate(() -> template.convertAndSendToUser(to.getUsername(), "/topic/game/challenge", from), 5000);
        LOGGER.debug("Broadcast challenge: {}", from);
    }

    @MessageMapping("/queue/game/challenge")
    public void handleCommand(String message, Principal principal) {
        User challenger = (User) ((Authentication) principal).getPrincipal();
        Command command = new Command(message);

        if (command.getCommand().equals("CHALLENGE.NEW")) {
            Optional<User> optional = userService.findOne(command.getIntegerData("userID"));

            if (!optional.isPresent()) {
                throw new ResourceNotFoundException("not found");
            }
            User challenged = optional.get();
            newChallenge(challenger, challenged);
        } else if (command.getCommand().equals("CHALLENGE.DENY")) {
//            deny(challenger);
        } else if (command.getCommand().equals("CHALLENGE.ACCEPT")) {
//            accept(challenger);
        }
        LOGGER.debug(command.getCommand());
        LOGGER.debug("userID {}" , command.getIntegerData("userID"));
    }

    public void newChallenge(User challenger, User challenged) {
        // Create unique Game ID

        // Store challenger and challenged against GameID in cache with timestamp

        // Send message to challenger containing gameID

        // send message to Challenged containing challenger info and game ID
    }

    public void deny(int gameID) {
        // Look up challenger and challenged using gameID

        // send message to challenger denying request
    }

    public void accept(int gameID) {

    }
}

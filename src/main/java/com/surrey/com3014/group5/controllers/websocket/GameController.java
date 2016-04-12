package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.dto.users.GamerDTO;
import com.surrey.com3014.group5.game.Command;
import com.surrey.com3014.group5.game.Game;
import com.surrey.com3014.group5.game.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import com.surrey.com3014.group5.models.impl.User;

import java.security.Principal;

/**
 * @author Aung Thu Moe
 */
@Controller
public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GameService gameService;

    @MessageMapping("/queue/game")
    public void request(String message, Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        Command command = new Command(message);
        if (Command.READY.equals(command.getCommand())) {
            Game game = gameService.getGame(command.getStringData("gameID"));
            // get the gamer using current client id
            GamerDTO gamer = game.getGamer(user.getId());
            // update the screen resolution of the client
            gamer.setWidth(command.getIntegerData("width"));
            gamer.setHeight(command.getIntegerData("height"));
            //TODO:: how to make sure both has set the resolution??
        }
    }

    public void response() {

    }
}

package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.dto.users.GamerDTO;
import com.surrey.com3014.group5.game.Command;
import com.surrey.com3014.group5.game.Game;
import com.surrey.com3014.group5.game.GameService;
import com.surrey.com3014.group5.game.Resolution;
import org.json.JSONObject;
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
            // record the gamer's resolution
            game.setGamerResolution(user.getId(), command.getIntegerData("height"), command.getIntegerData("width"));
            response(user.getId(), game);
        }
    }

    public void response(long gamerID, Game game) {
        Resolution resolution = game.getResolution();
        if (resolution != null) {
            JSONObject response = new JSONObject();
            response.put("gameID", game.getGameID());
            GamerDTO gamerDTO = game.getGamer(gamerID);
            response.put("role", gamerDTO.getRole());
            response.put("height", resolution.getHeight());
            response.put("width", resolution.getWidth());
            response.put("command", Command.PREP);
            template.convertAndSendToUser(game.getChallenger().getUsername(), "/topic/game", response.toString());
            template.convertAndSendToUser(game.getChallenged().getUsername(), "/topic/game", response.toString());
        }
    }
}

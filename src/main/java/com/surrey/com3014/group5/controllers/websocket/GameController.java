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
            response(game);
        } else if (Command.UPDATE.equals(command.getCommand())) {
            Game game = gameService.getGame(command.getStringData("gameID"));
            GamerDTO oppositePlayer = game.getOtherGamer(user.getId());

            final JSONObject response = new JSONObject();
            response.put("gameID", game.getGameID());
            response.put("vx", command.getDoubleData("vx"));
            response.put("vy", command.getDoubleData("vy"));
            response.put("magnitude", command.getIntegerData("magnitude"));
            response.put("rotation", command.getDoubleData("rotation"));
            response.put("command", Command.UPDATE);
            template.convertAndSendToUser(oppositePlayer.getUsername(), "/topic/game", response.toString());
        }
    }

    public void response(Game game) {
        Resolution resolution = game.getResolution();
        if (resolution != null) {
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
            template.convertAndSendToUser(game.getChallenged().getUsername(), "/topic/game", responseForChallenged.toString());
        }
    }
}

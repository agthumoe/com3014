package com.surrey.com3014.group5.websockets.controllers;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.dto.ActiveUserDTO;
import com.surrey.com3014.group5.websockets.services.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * Websocket controller to handle active online users.
 *
 * @author Aung Thu Moe
 */
@Controller
public class ActiveUsersController {


    /**
     * Active user service to store the active user in the cache.
     */
    @Autowired
    private ActiveUserService activeUserService;

    /**
     * This method schedule a fixed rate of 5000 milliseconds (5 sec) to send a push message to all user who
     * subscribed <code>/topic/active</code> channel.
     *
     * @return list of active users who are active within last 5 seconds.
     */
    @Scheduled(fixedRate = 5000)
    @SendTo("/topic/activeUsers")
    private List<ActiveUserDTO> broadcastActiveUsers() {
        return activeUserService.getActiveUsers();
    }

    /**
     * This method handles incoming message from <code>/queue/activeUsers</code>. User who sent message to
     * this channel will be noted as active in the activeUser list cache.
     *
     * @param message   the incoming message from the channel.
     * @param principal current user principal
     * @return list of active users who are active within last 5 seconds.
     */
    @MessageMapping("/queue/activeUsers")
    @SendTo("/topic/activeUsers")
    public List<ActiveUserDTO> activeUser(Message<Object> message, Principal principal) {
        final User user = (User) ((Authentication) principal).getPrincipal();
        activeUserService.update(new ActiveUserDTO(user));
        return activeUserService.getActiveUsers();
    }

}

package com.surrey.com3014.group5.websockets.controllers;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.dto.ActiveUserDTO;
import com.surrey.com3014.group5.websockets.services.ActiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ActiveUsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveUsersController.class);

    @Autowired
    private ActiveUserService activeUserService;

    @Scheduled(fixedRate = 5000)
    @SendTo("/topic/activeUsers")
    private List<ActiveUserDTO> broadcastActiveUsers() {
        return activeUserService.getActiveUsers();
    }

    @MessageMapping("/queue/activeUsers")
    @SendTo("/topic/activeUsers")
    public List<ActiveUserDTO> activeUser(Message<Object> message) {
        Principal principal = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        User user = (User) ((Authentication) principal).getPrincipal();
        activeUserService.update(new ActiveUserDTO(user));
        return activeUserService.getActiveUsers();
    }

}

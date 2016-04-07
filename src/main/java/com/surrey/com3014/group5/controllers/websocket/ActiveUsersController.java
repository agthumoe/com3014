package com.surrey.com3014.group5.controllers.websocket;

import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.activeuser.ActiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.security.Principal;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ActiveUsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveUsersController.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TaskScheduler scheduler;

    @Autowired
    private ActiveUserService activeUserService;

    @PostConstruct
    private void broadcastActiveUsers() {
        scheduler.scheduleAtFixedRate(() -> template.convertAndSend("/topic/activeUsers", activeUserService.getActiveUsers()), 10000);
        LOGGER.debug("Broadcast activeUsers: {}", activeUserService.getActiveUsers());
    }

    @MessageMapping("/queue/activeUsers")
    public void activeUser(Message<Object> message) {
        Principal principal = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        User user = (User) ((Authentication) principal).getPrincipal();
        activeUserService.mark(user);
        broadcastActiveUsers();
    }
}

package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.messages.MessageDTO;
import com.surrey.com3014.group5.dto.messages.ReplyMessageDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;

import static com.surrey.com3014.group5.configs.WebsocketConfig.IP_ADDRESS;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/websocket/tracker")
    @SendTo("/topic/global.chat")
    public ReplyMessageDTO greeting(MessageDTO message, StompHeaderAccessor stompHeaderAccessor, Principal principal) throws Exception {
        LOGGER.debug("Username: " + SecurityUtils.getCurrentUsername() + ", SessionID: " + stompHeaderAccessor.getSessionId() + ", IPAddress: " + stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        User user = (User) ((Authentication) principal).getPrincipal();
        return new ReplyMessageDTO(user.getUsername(), new Date(), message.getMessage());
    }
}

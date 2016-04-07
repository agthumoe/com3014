package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.messages.MessageDTO;
import com.surrey.com3014.group5.dto.messages.ReplyMessageDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.activeuser.ActiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Set;

import static com.surrey.com3014.group5.configs.WebsocketConfig.IP_ADDRESS;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ActiveUserService activeUserService;

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @MessageMapping("/queue/chat")
    @SendTo("/topic/chat")
    public ReplyMessageDTO greeting(MessageDTO message, StompHeaderAccessor stompHeaderAccessor, Principal principal) throws Exception {
        LOGGER.debug("Username: " + SecurityUtils.getCurrentUsername() + ", SessionID: " + stompHeaderAccessor.getSessionId() + ", IPAddress: " + stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        User user = (User) ((Authentication) principal).getPrincipal();
        Instant instant = Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis());
        return new ReplyMessageDTO(user.getUsername(), dateTimeFormatter.format(ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())), message.getMessage());
    }

    @MessageMapping("/queue/activeUsers")
    @SendTo("/topic/activeUsers")
    public Set<String> getActiveUsers(Message<Object> message) {
        Principal principal = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
        User user = (User) ((Authentication) principal).getPrincipal();
        activeUserService.mark(user.getUsername());
        return activeUserService.getActiveUsers();
    }
}

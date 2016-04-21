package com.surrey.com3014.group5.websockets.controllers;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.dto.MessageDTO;
import com.surrey.com3014.group5.websockets.dto.ReplyMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ChatController {

    /**
     * Datetime formatter.
     */
    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    /**
     * This method accepts the incoming message from the websocket channel <code>/queue/chat</code> and
     * redirects the message back to <code>/topic/chat</code> channel.
     *
     * @param message   The incoming message.
     * @param principal current user principal
     * @return the outgoing message to all users subscribing the channel.
     */
    @MessageMapping("/queue/chat")
    @SendTo("/topic/chat")
    public ReplyMessageDTO chat(MessageDTO message, Principal principal) {
        final String escapedMessage = escapeHtml4(message.getMessage());
        User user = (User) ((Authentication) principal).getPrincipal();
        Instant instant = Instant.ofEpochMilli(Calendar.getInstance().getTimeInMillis());
        return new ReplyMessageDTO(new UserDTO(user), dateTimeFormatter.format(ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())), escapedMessage);
    }

}

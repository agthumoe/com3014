package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ChatController {

    @MessageMapping("/websocket/tracker")
    @SendTo("/topic/global.chat")
    public String greeting(ChatMessage message) throws Exception {
        return "[" + new Timestamp(System.currentTimeMillis()) + "]: " + message.getMessage();
    }
}

package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.Greeting;
import com.surrey.com3014.group5.dto.HelloMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author Aung Thu Moe
 */
@Controller
public class ChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/websocket/tracker")
    @SendTo("/topic/global.chat")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + message.getName() + "!");
    }
}

package com.surrey.com3014.group5.dto;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class HelloMessage implements Message<String> {

    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String getPayload() {
        return null;
    }

    @Override
    public MessageHeaders getHeaders() {
        return null;
    }
}

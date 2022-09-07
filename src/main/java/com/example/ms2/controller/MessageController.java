package com.example.ms2.controller;

import com.example.ms2.controller.request.MessageRequest;
import com.example.ms2.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageService service;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MessageController(MessageService service) {
        this.service = service;
    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public void broadcastNews(@Payload MessageRequest message) {
        logger.info("MS2 received message");
        service.put(message);
    }
}

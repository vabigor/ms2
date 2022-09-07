package com.example.ms2.service.impl;

import com.example.ms2.controller.request.MessageRequest;
import com.example.ms2.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    private final KafkaTemplate<String, MessageRequest> kafkaTemplate;

    @Value("${kafka.topic}")
    private String TOPIC;

    @Autowired
    public MessageServiceImpl(KafkaTemplate<String, MessageRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void put(MessageRequest messageRequest) {
        messageRequest.setMs2Timestamp(new Date());
        try {
            Message<MessageRequest> message = MessageBuilder
                    .withPayload(messageRequest)
                    .setHeader(KafkaHeaders.TOPIC, TOPIC)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
                    .build();
            ListenableFuture<SendResult<String, MessageRequest>> test = kafkaTemplate.send(message);
            SendResult<String, MessageRequest> result = test.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

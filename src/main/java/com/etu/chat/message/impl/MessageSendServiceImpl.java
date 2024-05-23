package com.etu.chat.message.impl;

import com.etu.chat.entity.json_view.Views;
import com.etu.chat.message.MessageEvent;
import com.etu.chat.message.MessageSendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
class MessageSendServiceImpl implements MessageSendService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService messageExecutor = Executors.newWorkStealingPool();
    private final ObjectMapper objectMapper;

    @PreDestroy
    void destroy() {
        messageExecutor.shutdown();
    }

    @Override
    public void sendToRoom(MessageEvent messageEvent) {
        messageExecutor.submit(createMessageSendTask(messageEvent));
    }

    private Runnable createMessageSendTask(MessageEvent messageEvent) {
        ObjectWriter writer = objectMapper.setConfig(objectMapper.getSerializationConfig())
                .writerWithView(Views.Low.class);

        return () -> {
            String messageEventAsString;

            try {
                messageEventAsString = writer.writeValueAsString(messageEvent);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(messageEvent.getMessage().getRoomId()),
                    "/messages",
                    messageEventAsString
            );

            log.info("Send message into topic: /room/{}/messages", messageEvent.getMessage().getRoomId());
        };
    }
}

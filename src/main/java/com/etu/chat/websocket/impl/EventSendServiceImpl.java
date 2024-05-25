package com.etu.chat.websocket.impl;

import com.etu.chat.entity.json_view.Views;
import com.etu.chat.websocket.Event;
import com.etu.chat.websocket.EventSendService;
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
class EventSendServiceImpl implements EventSendService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ExecutorService messageExecutor = Executors.newWorkStealingPool();
    private final ObjectMapper objectMapper;

    @PreDestroy
    void destroy() {
        messageExecutor.shutdown();
    }

    private Runnable createMessageSendTask(Event<?> event, ObjectWriter objectWriter) {
        return () -> {
            String eventAsString;

            ObjectWriter writer = objectWriter != null ? objectWriter : objectMapper.writer();

            try {
                eventAsString = writer.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            messagingTemplate.convertAndSend(event.getUrl(), eventAsString);

            log.info("Send event {} to {}", event, event.getUrl());
        };
    }

    @Override
    public void send(Event<?> event) {
        messageExecutor.submit(createMessageSendTask(event, null));
    }

    @Override
    public void send(Event<?> event, ObjectWriter writer) {
        messageExecutor.submit(createMessageSendTask(event, writer));
    }
}

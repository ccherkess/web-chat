package com.etu.chat.message.impl;

import com.etu.chat.message.MessageEvent;
import com.etu.chat.message.MessageSendService;
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

    @PreDestroy
    void destroy() {
        messageExecutor.shutdown();
    }

    @Override
    public void sendToRoom(MessageEvent messageEvent) {
        messageExecutor.submit(createMessageSendTask(messageEvent));
    }

    private Runnable createMessageSendTask(MessageEvent messageEvent) {
            return () -> {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(messageEvent.getMessage().getRoomId()),
                        "/messages",
                        messageEvent
                );

                log.info("Send message into topic: /room/{}/messages", messageEvent.getMessage().getRoomId());
            };
    }
}

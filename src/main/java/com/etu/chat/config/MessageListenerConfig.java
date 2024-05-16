package com.etu.chat.config;

import com.etu.chat.service.MessageListenerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerConfig {

    @Bean
    public CommandLineRunner startListener(MessageListenerService messageListenerService) {
        return (args) -> {
            Runnable listener = messageListenerService.createMessageHandler();
            new Thread(listener, "message-queue-listener").start();
        };
    }

}
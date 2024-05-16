package com.etu.chat.config;

import com.etu.chat.service.MessageListenerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;

@Configuration
public class MessageListenerConfig {

    @Bean
    public CommandLineRunner startListener(MessageListenerService messageListenerService) {

        String encoding = Base64.getEncoder().encodeToString(("admin:admin").getBytes());
        String authHeader = "Basic " + encoding;
        System.out.println(authHeader);

        System.out.println(new BCryptPasswordEncoder().encode("admin"));

        return (args) -> {
            Runnable listener = messageListenerService.createMessageHandler();
            new Thread(listener, "message-queue-listener").start();
        };
    }

}
package com.etu.chat.queue_listener.message;

import com.etu.chat.entity.Message;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.websocket.Event;
import com.etu.chat.websocket.EventSendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
@RequiredArgsConstructor
class MessageQueueListener {
    private static final String MESSAGE_QUEUE_CHANNEL = "message_queue";

    private final DataSource dataSource;
    private final EventSendService messageSendService;
    private final MessageEventDeserializer messageEventDeserializer;
    private final ObjectMapper objectMapper;
    private final Thread listenerThread = new Thread(this::messageHandler, "message-listener");

    private Connection connection;

    @PostConstruct
    void initialize() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MessageEvent.class, messageEventDeserializer);
        objectMapper.registerModule(module);

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        listenerThread.start();
    }

    @PreDestroy
    void destroy() {
       listenerThread.interrupt();

        log.info("Stop listening message from channel {}", MESSAGE_QUEUE_CHANNEL);

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void messageHandler() {
        try (Statement statement = connection.createStatement()) {

            statement.execute("LISTEN " + MESSAGE_QUEUE_CHANNEL);

            log.info("Start listening message from channel {}", MESSAGE_QUEUE_CHANNEL);

            PGConnection pgconn = connection.unwrap(PGConnection.class);

            while(!Thread.currentThread().isInterrupted()) {
                PGNotification[] messages = pgconn.getNotifications(500);
                if (messages == null) {
                    continue;
                }
                for(PGNotification message : messages) {
                    messageProcessing(message);
                }
            }

        } catch (SQLException | JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private void messageProcessing(PGNotification message) throws JsonProcessingException {
        Event<Message> messageEvent = objectMapper.readValue(message.getParameter(), MessageEvent.class);

        log.info("Received message: {} from channel: {}", messageEvent, MESSAGE_QUEUE_CHANNEL);

        messageSendService.send(
                messageEvent, objectMapper.setConfig(objectMapper.getSerializationConfig()).writerWithView(Views.Low.class)
        );
    }
}

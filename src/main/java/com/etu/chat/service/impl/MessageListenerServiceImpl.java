package com.etu.chat.service.impl;

import com.etu.chat.service.MessageListenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
@Slf4j
@RequiredArgsConstructor
class MessageListenerServiceImpl implements MessageListenerService {

    private static final String MESSAGE_QUEUE_CHANNEL = "message_queue";

    private final DataSource dataSource;

    @Override
    public Runnable createMessageHandler() {
        return  () -> {
                try (Connection connection = dataSource.getConnection()) {
                    startListenerAction(connection);
                } catch (Exception e) {
                    log.warn("Error message listener!", e);
                }
        };
    }

    private void startListenerAction(Connection c) throws SQLException {
        c.createStatement().execute("LISTEN " + MESSAGE_QUEUE_CHANNEL);

        log.info("Start listening message from channel {}", MESSAGE_QUEUE_CHANNEL);

        PGConnection pgconn = c.unwrap(PGConnection.class);

        while(!Thread.currentThread().isInterrupted()) {
            PGNotification[] messages = pgconn.getNotifications(0);
            if (messages == null) {
                continue;
            }
            for(PGNotification message : messages) {
                messageProcessing(message);
            }
        }

    }

    private void messageProcessing(PGNotification message) {
        System.out.println(message.getParameter());
    }

}

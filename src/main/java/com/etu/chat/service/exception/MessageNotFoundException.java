package com.etu.chat.service.exception;

import com.etu.chat.entity.Message;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(long id) {
        super("Not found message with id = %d".formatted(id));
    }
}

package com.etu.chat.message;

import com.etu.chat.entity.Message;

public interface MessageEvent {

    enum EventType {
        CREATE, UPDATE, DELETE;
    }

    EventType getEventType();

    Message getMessage();

}
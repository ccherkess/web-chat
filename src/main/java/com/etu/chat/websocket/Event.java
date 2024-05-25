package com.etu.chat.websocket;

import com.fasterxml.jackson.databind.ObjectWriter;

public interface Event<T> {

    enum EventType {
        CREATE, UPDATE, DELETE;
    }

    EventType getEventType();

    T getPayload();

    String getUrl();

}
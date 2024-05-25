package com.etu.chat.websocket;

import com.fasterxml.jackson.databind.ObjectWriter;

public interface EventSendService {

    void send(Event<?> event);

    void send(Event<?> event, ObjectWriter writer);

}

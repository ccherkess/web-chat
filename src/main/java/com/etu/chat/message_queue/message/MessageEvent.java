package com.etu.chat.message_queue.message;

import com.etu.chat.entity.Message;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.websocket.Event;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonView(Views.Low.class)
@AllArgsConstructor
@NoArgsConstructor
class MessageEvent implements Event<Message> {
    private final static String URL_FORMAT = "/room/%d/messages";

    private EventType eventType;
    private Message payload;

    @Override
    public String getUrl() {
        return URL_FORMAT.formatted(payload.getRoomId());
    }
}

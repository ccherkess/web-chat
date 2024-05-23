package com.etu.chat.message.impl;

import com.etu.chat.entity.Message;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.message.MessageEvent;
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
class MessageEventImpl implements MessageEvent {
    private EventType eventType;
    private Message message;
}

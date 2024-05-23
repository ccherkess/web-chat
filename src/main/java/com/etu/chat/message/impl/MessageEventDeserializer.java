package com.etu.chat.message.impl;

import com.etu.chat.entity.Message;
import com.etu.chat.message.MessageEvent;
import com.etu.chat.service.ChatUserService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
class MessageEventDeserializer extends JsonDeserializer<MessageEventImpl> {

    private final ChatUserService chatUserService;

    @Override
    public MessageEventImpl deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        MessageEventImpl messageEvent = new MessageEventImpl();
        Message message = new Message();

        JsonNode node = p.getCodec().readTree(p);
        messageEvent.setEventType(MessageEvent.EventType.valueOf(node.get("eventType").asText()));

        node = node.get("message");
        message.setId(node.get("id").asLong());
        message.setRoomId(node.get("c_room_id").asLong());
        message.setUser(chatUserService.find(node.get("c_user_id").asLong()).orElseThrow());
        message.setPayload(node.get("c_payload").textValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        message.setSendAt(LocalDateTime.parse(node.get("c_send_at").asText().split("\\.")[0], formatter));

        messageEvent.setMessage(message);

        return messageEvent;
    }
}

package com.etu.chat.message.impl;

import com.etu.chat.entity.Message;
import com.etu.chat.message.MessageEvent;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = MessageEventImpl.MessageEventDeserializer.class)
class MessageEventImpl implements MessageEvent {
    private EventType eventType;
    private Message message;


    static class MessageEventDeserializer extends JsonDeserializer<MessageEventImpl> {
        @Override
        public MessageEventImpl deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            MessageEventImpl messageEvent = new MessageEventImpl();
            Message message = new Message();

            JsonNode node = p.getCodec().readTree(p);
            messageEvent.setEventType(MessageEvent.EventType.valueOf(node.get("eventType").asText()));

            node = node.get("message");
            message.setId(node.get("id").asLong());
            message.setRoomId(node.get("c_room_id").asLong());
            message.setUserId(node.get("c_user_id").asLong());
            message.setPayload(node.get("c_payload").textValue());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            message.setSendAt(LocalDateTime.parse(node.get("c_send_at").asText().split("\\.")[0], formatter));

            messageEvent.setMessage(message);

            return messageEvent;
        }
    }
}

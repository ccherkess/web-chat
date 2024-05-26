package com.etu.chat.service;

import com.etu.chat.entity.Message;

public interface MessageService {

    Iterable<Message> getMessagesFromRoom(long roomId, int count);

    Iterable<Message> getMessagesFromRoom(long roomId, int count, long startId);

    Message save(Message message, String username);

    Message edit(Message message);

    Message delete(Message message);

    boolean canEdit(String username, Long id);
}

package com.etu.chat.service.impl;

import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Message;
import com.etu.chat.entity.Room;
import com.etu.chat.repository.MessageRepository;
import com.etu.chat.repository.RoomRepository;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.MessageService;
import com.etu.chat.service.RoomService;
import com.etu.chat.service.exception.MessageNotFoundException;
import com.etu.chat.service.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("messageService")
@RequiredArgsConstructor
class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final ChatUserService chatUserService;

    @Override
    public Iterable<Message> getMessagesFromRoom(long roomId, int count, long startId) {
        messageRepository.findById(startId).orElseThrow(() -> new MessageNotFoundException(startId));

        return messageRepository.findByRoomIdAndIdBeforeOrderByIdDesc(roomId, startId, Pageable.ofSize(count));
    }

    @Override
    public Iterable<Message> getMessagesFromRoom(long roomId, int count) {
        return messageRepository.findByRoomIdOrderByIdDesc(roomId, Pageable.ofSize(count));
    }

    @Override
    public Message save(Message message, String username) {
        roomService.getRoom(message.getRoomId());
        ChatUser chatUser = chatUserService.find(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return messageRepository.save(Message.builder()
                .payload(message.getPayload())
                .roomId(message.getRoomId())
                .user(chatUser)
                .build());
    }

    @Override
    @Transactional
    public Message edit(Message message) {
        return messageRepository.findById(message.getId())
                .map(m -> {
                    m.setPayload(message.getPayload());
                    return messageRepository.save(m);
                }).orElseThrow(() -> new MessageNotFoundException(message.getId()));
    }

    @Override
    public Message delete(Message message) {
        return messageRepository.findById(message.getId())
                .map(m -> {
                    messageRepository.delete(m);
                    return m;
                }).orElseThrow(() -> new MessageNotFoundException(message.getId()));
    }

    @Override
    @Transactional
    public boolean canEdit(String username, Long id) {
        return messageRepository.findById(id)
                .map(message -> {
                    Room room = roomService.getRoom(message.getRoomId());
                    return chatUserService.isCanWriteRoom(username, room)
                            && message.getUser().getName().equals(username);
                })
                .orElse(false);
    }
}

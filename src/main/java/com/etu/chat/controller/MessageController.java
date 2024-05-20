package com.etu.chat.controller;

import com.etu.chat.entity.Message;
import com.etu.chat.repository.MessageRepository;
import com.etu.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(consumes = "application/json", path = "api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/{roomId}/{page}")
    public Iterable<Message> getRoomMessage(@PathVariable Long roomId, @PathVariable Integer page) {
        return messageRepository.findByRoomId(roomId, PageRequest.of(page, 25));
    }

    @PostMapping("/{roomId}/send")
    public Message sendToRoom(@RequestBody Message message) {
        roomRepository.findById(message.getRoomId()).orElseThrow();

        return messageRepository.save(message);
    }

    @PutMapping("/edit")
    public Message editMessage(@RequestBody Message message) {
        Optional<Message> messageFromDb = messageRepository.findById(message.getId());

        messageFromDb.ifPresent(m -> m.setPayload(message.getPayload()));

        return messageRepository.save(messageFromDb.orElseThrow());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMessage(@RequestBody Message message) {
        Message messageFromDB = messageRepository.findById(message.getId()).orElseThrow(() -> new RuntimeException("Message not found!"));

        messageRepository.delete(message);

        return ResponseEntity.ok("Message deleted successfully!");
    }
}

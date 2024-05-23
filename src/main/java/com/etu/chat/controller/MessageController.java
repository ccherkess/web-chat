package com.etu.chat.controller;

import com.etu.chat.entity.Message;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.service.MessageService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = "application/json", path = "api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @JsonView(Views.Low.class)
    @GetMapping(value = {"/{roomId}/{count}", "/{roomId}/{count}/{startId}"})
    public Iterable<Message> getRoomMessage(@PathVariable Long roomId, @PathVariable Integer count, @PathVariable(required = false) Long startId) {
        return startId != null
                ? messageService.getMessagesFromRoom(roomId, count, startId)
                : messageService.getMessagesFromRoom(roomId, count);
    }

    @JsonView(Views.Low.class)
    @PostMapping("/send")
    public Message sendToRoom(@RequestBody Message message, @AuthenticationPrincipal User user) {
        return messageService.save(message, user.getUsername());
    }

    @JsonView(Views.Low.class)
    @PutMapping("/edit")
    public Message editMessage(@RequestBody Message message) {
        return messageService.edit(message);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMessage(@RequestBody Message message) {
        messageService.delete(message);

        return ResponseEntity.ok("Message deleted successfully!");
    }
}

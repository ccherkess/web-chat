package com.etu.chat.controller;

import com.etu.chat.dto.UserRoomAuthority;
import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.RoomService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = "application/json", path = "api/user")
@RequiredArgsConstructor
public class UserController {

    private final ChatUserService chatUserService;
    private final RoomService roomService;

    @GetMapping("/authority/{roomId:\\d+}")
    public UserRoomAuthority getUserRoomAuthority(@PathVariable Long roomId, @AuthenticationPrincipal User user) {
        Room room = roomService.getRoom(roomId);

        return UserRoomAuthority.builder()
                .canRead(chatUserService.isCanReadRoom(user.getUsername(), room))
                .canWrite(chatUserService.isCanWriteRoom(user.getUsername(), room))
                .build();
    }

    @GetMapping(value = {"/all/{count:\\d+}", "/all/{count:\\d+}/{startId:\\d+}"})
    @JsonView(Views.Low.class)
    public Iterable<ChatUser> getUsers(@PathVariable Integer count, @PathVariable(required = false) Long startId) {
        return startId != null
                ? chatUserService.getUsers(count, startId)
                : chatUserService.getUsers(count);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        chatUserService.delete(username);

        return ResponseEntity.ok("User deleted");
    }

    @GetMapping("/room/not/{roomId:\\d+}")
    @JsonView(Views.Low.class)
    public Iterable<ChatUser> getUsersNotInRoom(@PathVariable Long roomId) {
        return chatUserService.findAll().stream()
                .filter(chatUser -> !chatUserService.isInRoom(chatUser.getName(), roomService.getRoom(roomId)))
                .toList();
    }
}

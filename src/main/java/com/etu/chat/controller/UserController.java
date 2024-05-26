package com.etu.chat.controller;

import com.etu.chat.dto.UserRoomAuthority;
import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/room/not/{roomId:\\d+}")
    public Iterable<ChatUser> getUsersNotInRoom(@PathVariable Long roomId) {
        return chatUserService.findAll().stream()
                .filter(chatUser -> !chatUserService.isInRoom(chatUser.getName(), roomService.getRoom(roomId)))
                .toList();
    }
}

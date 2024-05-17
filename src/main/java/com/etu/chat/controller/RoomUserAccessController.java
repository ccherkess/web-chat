package com.etu.chat.controller;

import com.etu.chat.entity.Room;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = "application/json", path = "api/room/user")
@RequiredArgsConstructor
public class RoomUserAccessController {
    private final RoomService roomService;
    private final ChatUserService chatUserService;

    @PutMapping("/add/{username}")
    public ResponseEntity<String> addUser(@RequestBody Room room, @PathVariable String username) {
        roomService.addUser(room, username);

        return ResponseEntity.ok("User add into room successfully!");
    }

    @PutMapping("/enable/write/{username}")
    public ResponseEntity<String> enableWriteUser(@RequestBody Room room, @PathVariable String username) {
        chatUserService.allowWriteRoom(room, username, true);

        return ResponseEntity.ok("User enable write room successfully!");
    }

    @PutMapping("/disable/write/{username}")
    public ResponseEntity<String> disableWriteUser(@RequestBody Room room, @PathVariable String username) {
        chatUserService.allowWriteRoom(room, username, false);

        return ResponseEntity.ok("User disable write room successfully!");
    }

    @PutMapping("/delete/{username}")
    public ResponseEntity<String> deleteUser(@RequestBody Room room, @PathVariable String username) {
        roomService.deleteUser(room, username);

        return ResponseEntity.ok("User delete from room successfully!");
    }
}

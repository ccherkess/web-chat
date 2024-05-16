package com.etu.chat.controller;

import com.etu.chat.entity.Room;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.RoomService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = "application/json", path = "api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final ChatUserService chatUserService;

    @GetMapping
    @JsonView(Views.Low.class)
    public Iterable<Room> getAvailableRooms(@AuthenticationPrincipal UserDetails userDetails) {
        return roomService.getAvailableRooms(userDetails.getUsername());
    }

    @GetMapping("/info/{id}")
    @JsonView(Views.Medium.class)
    public Room getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }

    @PostMapping("/create")
    @JsonView(Views.Low.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRoomById(@RequestBody Room room) {
        roomService.deleteRoom(room);

        return ResponseEntity.ok("Room deleted successfully!");
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editRoomName(@RequestBody Room room) {
        roomService.editRoomName(room);

        return ResponseEntity.ok("Room name edit successfully!");
    }

    @PutMapping("/users/add/{username}")
    public ResponseEntity<String> addUser(@RequestBody Room room, @PathVariable String username) {
        roomService.addUser(room, username);

        return ResponseEntity.ok("User add into room successfully!");
    }

    @PutMapping("/users/enable/write/{username}")
    public ResponseEntity<String> enableWriteUser(@RequestBody Room room, @PathVariable String username) {
        chatUserService.allowWriteRoom(room, username, true);

        return ResponseEntity.ok("User enable write room successfully!");
    }

    @PutMapping("/users/enable/read/{username}")
    public ResponseEntity<String> enableReadUser(@RequestBody Room room, @PathVariable String username) {
        chatUserService.allowReadRoom(room, username, true);

        return ResponseEntity.ok("User enable read room successfully!");
    }

    @PutMapping("/users/disable/write/{username}")
    public ResponseEntity<String> disableWriteUser(@RequestBody Room room, @PathVariable String username) {
        chatUserService.allowWriteRoom(room, username, false);

        return ResponseEntity.ok("User disable write room successfully!");
    }

    @PutMapping("/users/disable/read/{username}")
    public ResponseEntity<String> disableReadUser(@RequestBody Room room, @PathVariable String username) {
        chatUserService.allowReadRoom(room, username, false);

        return ResponseEntity.ok("User disable read room successfully!");
    }

    @PutMapping("/users/delete/{username}")
    public ResponseEntity<String> deleteUser(@RequestBody Room room, @PathVariable String username) {
        roomService.deleteUser(room, username);

        return ResponseEntity.ok("User delete from room successfully!");
    }
}

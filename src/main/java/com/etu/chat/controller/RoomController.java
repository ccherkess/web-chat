package com.etu.chat.controller;

import com.etu.chat.entity.Room;
import com.etu.chat.entity.json_view.Views;
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
}
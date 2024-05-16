package com.etu.chat.controller;

import com.etu.chat.entity.Room;
import com.etu.chat.entity.json_view.RoomViews;
import com.etu.chat.service.RoomService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = "application/json", path = "api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @JsonView(RoomViews.IdName.class)
    public Iterable<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{name}")
    @JsonView(RoomViews.IdName.class)
    public Iterable<Room> getRoomsByName(@PathVariable String name) {
        return roomService.findRoomsByPartialMatchName(name);
    }

    @GetMapping("/{id::+d}")
    public Room getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }


    @PostMapping("/create")
    @JsonView(RoomViews.IdName.class)
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
    @JsonView(RoomViews.IdName.class)
    public Room editRoomName(@RequestBody Room room) {
        return roomService.editRoomName(room);
    }

}

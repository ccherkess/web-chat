package com.etu.chat.controller;

import com.etu.chat.dto.RoomWithUsers;
import com.etu.chat.dto.UserWithAuthority;
import com.etu.chat.entity.Authority;
import com.etu.chat.entity.Room;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.service.AuthorityService;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.RoomService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(consumes = "application/json", path = "api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final AuthorityService authorityService;
    private final ChatUserService chatUserService;

    @GetMapping
    @JsonView(Views.Low.class)
    public Iterable<Room> getAvailableRooms(@AuthenticationPrincipal UserDetails userDetails) {
        return roomService.getAvailableRooms(userDetails.getUsername());
    }

    @GetMapping("/info/full")
    public Iterable<RoomWithUsers> getRooms() {
        return roomService.getRooms().stream().map(this::roomWithFilteredUsers).toList();
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

    private RoomWithUsers roomWithFilteredUsers(Room room) {
        RoomWithUsers roomForClient = new RoomWithUsers();

        roomForClient.setName(room.getName());
        roomForClient.setId(room.getId());

        Authority forWrite = authorityService.getAuthorityForWriteRoom(room);
        List<UserWithAuthority> chatUsers = new ArrayList<>();
        room.getChatUsers().forEach(user -> {
            chatUsers.add(UserWithAuthority.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .authorities(chatUserService.isCanWriteRoom(user.getName(), room) ? List.of(forWrite) : List.of())
                    .build());
        });
        roomForClient.setChatUsers(chatUsers);

        return roomForClient;
    }
}

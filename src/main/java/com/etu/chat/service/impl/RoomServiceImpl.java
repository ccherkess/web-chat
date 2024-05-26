package com.etu.chat.service.impl;

import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;
import com.etu.chat.repository.RoomRepository;
import com.etu.chat.service.AuthorityService;
import com.etu.chat.service.ChatUserService;
import com.etu.chat.service.RoomService;
import com.etu.chat.service.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roomService")
@RequiredArgsConstructor
class RoomServiceImpl implements RoomService {

    private final static String ADMIN = "admin";

    private final RoomRepository roomRepository;
    private final AuthorityService authorityService;
    private final ChatUserService chatUserService;

    @Override
    public Iterable<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    @Transactional
    public Iterable<Room> getAvailableRooms(String username) {
        return chatUserService.find(username)
                .map(ChatUser::getRooms).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    @Transactional
    public Room createRoom(Room room) {
        Room newRoom = roomRepository.save(Room.builder()
                .name(room.getName())
                .build());

        authorityService.createAuthorityForRoom(newRoom);

        addUser(newRoom, ADMIN);
        chatUserService.allowWriteRoom(newRoom, ADMIN, true);

        return newRoom;
    }

    @Override
    @Transactional
    public void deleteRoom(Room room) {
        Room roomFromDB = roomRepository.findById(room.getId())
                .orElseThrow(() -> new RoomNotFoundException(room.getId()));

        authorityService.deleteAuthorityForRoom(roomFromDB);

        roomRepository.delete(roomFromDB);
    }

    @Override
    public void editRoomName(Room room) {
        roomRepository.save(roomRepository.findById(room.getId())
                .map(r -> {
                    r.setName(room.getName());
                    return r;
                }).orElseThrow(() -> new RoomNotFoundException(room.getId())));
    }

    @Override
    public Room getRoom(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Override
    @Transactional
    public void addUser(Room room, String username) {
        Room roomFromDB = roomRepository.findById(room.getId())
                .orElseThrow(() -> new RoomNotFoundException(room.getId()));

        chatUserService.find(username).ifPresentOrElse(
                user -> {
                    roomFromDB.addUser(user);
                    roomRepository.save(roomFromDB);
                    chatUserService.allowReadRoom(roomFromDB, username, true);
                },
                () -> {
                    throw new UsernameNotFoundException(username);
                }
        );
    }

    @Override
    @Transactional
    public void deleteUser(Room room, String username) {
        Room roomFromDB = roomRepository.findById(room.getId())
                .orElseThrow(() -> new RoomNotFoundException(room.getId()));

        chatUserService.find(username).ifPresentOrElse(
                user -> {
                    roomFromDB.getChatUsers().remove(user);
                    roomRepository.save(roomFromDB);
                    chatUserService.allowReadRoom(roomFromDB, username, false);
                    chatUserService.allowWriteRoom(roomFromDB, username, false);
                },
                () -> {
                    throw new UsernameNotFoundException(username);
                }
        );
    }
}

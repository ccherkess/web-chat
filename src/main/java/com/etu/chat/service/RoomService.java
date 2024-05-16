package com.etu.chat.service;

import com.etu.chat.entity.Room;

public interface RoomService {
    Iterable<Room> getAvailableRooms(String username);

    Room getRoom(Long id);

    Room createRoom(Room room);

    void deleteRoom(Room room);

    void editRoomName(Room room);

    void addUser(Room room, String username);

    void deleteUser(Room room, String username);
}

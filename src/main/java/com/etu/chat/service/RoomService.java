package com.etu.chat.service;

import com.etu.chat.entity.Room;

import java.util.List;

public interface RoomService {
    List<Room> getRooms();

    List<Room> getAvailableRooms(String username);

    Room getRoom(Long id);

    Room createRoom(Room room);

    void deleteRoom(Room room);

    void editRoomName(Room room);

    void addUser(Room room, String username);

    void deleteUser(Room room, String username);
}

package com.etu.chat.service;

import com.etu.chat.entity.Room;

public interface RoomService {
    Iterable<Room> getRooms();

    Iterable<Room> findRoomsByPartialMatchName(String name);

    Room createRoom(Room room);

    void deleteRoom(Room room);

    Room editRoomName(Room room);

    Room getRoom(Long id);
}

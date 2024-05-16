package com.etu.chat.service.impl;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.Room;
import com.etu.chat.repository.AuthorityRepository;
import com.etu.chat.repository.RoomRepository;
import com.etu.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public Iterable<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Iterable<Room> findRoomsByPartialMatchName(String name) {
        return StreamSupport.stream(roomRepository.findAll().spliterator(), false)
                .filter(room -> room.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Override
    @Transactional
    public Room createRoom(Room room) {
        Room newRoom = roomRepository.save(room);

        authorityRepository.save(Authority.builder().
                name("CAN_CRUD_" + room.getId())
                .build());

        return newRoom;
    }

    @Override
    @Transactional
    public void deleteRoom(Room room) {
        Room roomFromDB = roomRepository.findById(room.getId())
                .orElseThrow(() -> notFoundRoomException(room.getId()));

        authorityRepository.deleteByName("CAN_CRUD_" + roomFromDB.getId());

        roomRepository.delete(roomFromDB);
    }

    @Override
    public Room editRoomName(Room room) {
        return roomRepository.save(roomRepository.findById(room.getId())
                .map(r -> {
                    r.setName(room.getName());
                    return r;
                }).orElseThrow(() -> notFoundRoomException(room.getId())));
    }

    @Override
    public Room getRoom(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> notFoundRoomException(id));
    }

    private RuntimeException notFoundRoomException(long id) {
        return new RuntimeException("Not found room with id = %s".formatted(id));
    }
}

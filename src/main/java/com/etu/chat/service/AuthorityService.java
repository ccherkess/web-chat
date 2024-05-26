package com.etu.chat.service;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.Room;

public interface AuthorityService {
    enum AuthorityPattern {
        READ_ROOM,
        WRITE_ROOM
    }

    Authority getAccessAuthorityRoom(AuthorityPattern pattern, Room room);

    Authority getAuthorityForReadRoom(Room room);

    Authority getAuthorityForWriteRoom(Room room);

    void createAuthorityForRoom(Room room);

    void deleteAuthorityForRoom(Room room);
}

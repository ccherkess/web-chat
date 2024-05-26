package com.etu.chat.service;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;

import java.util.List;
import java.util.Optional;

public interface ChatUserService {
    List<ChatUser> findAll();

    Optional<ChatUser> find(String username);

    Optional<ChatUser> find(long id);

    boolean isInRoom(String username, Room room);

    void addAuthorityForUser(String username, Authority authority);

    void removeAuthorityForUser(String username, Authority authority);

    boolean hasAuthority(String username, Authority authority);

    boolean isCanReadRoom(String username, Room room);

    void allowReadRoom(Room room, String username, boolean isAllow);

    boolean isCanWriteRoom(String username, Room room);

    void allowWriteRoom(Room room, String username, boolean isAllow);
}

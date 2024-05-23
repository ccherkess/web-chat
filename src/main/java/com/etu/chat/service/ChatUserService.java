package com.etu.chat.service;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;
import com.etu.chat.dto.UserWithAuthorities;

import java.util.Optional;

public interface ChatUserService {
    Optional<ChatUser> find(String username);

    Optional<ChatUser> find(long id);

    void addAuthorityForUser(String username, Authority authority);

    void removeAuthorityForUser(String username, Authority authority);

    boolean hasAuthority(String username, Authority authority);

    boolean isCanReadRoom(String username, Room room);

    void allowReadRoom(Room room, String username, boolean isAllow);

    boolean isCanWriteRoom(String username, Room room);

    void allowWriteRoom(Room room, String username, boolean isAllow);

    UserWithAuthorities serializeUserWithAuthorities(ChatUser user, Room room);
}

package com.etu.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomWithUsers {
    private long id;
    private String name;
    private List<UserWithAuthority> chatUsers;
}

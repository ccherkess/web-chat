package com.etu.chat.dto;

import java.util.List;

public class RoomWithUsers {
    private Long roomId;
    private String name;
    private List<UserWithAuthorities> users;

    // Constructor, getters, and setters
    public RoomWithUsers(Long roomId, String name, List<UserWithAuthorities> users) {
        this.roomId = roomId;
        this.name = name;
        this.users = users;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserWithAuthorities> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithAuthorities> users) {
        this.users = users;
    }
}

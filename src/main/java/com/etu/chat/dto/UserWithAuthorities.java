package com.etu.chat.dto;

public class UserWithAuthorities {
    private String name;
    private boolean read;
    private boolean write;

    public UserWithAuthorities(String name, boolean read, boolean write) {
        this.name = name;
        this.read = read;
        this.write = write;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }
}

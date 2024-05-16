package com.etu.chat.service.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(long id) {
        super("Not found room with id = %d".formatted(id));
    }
}

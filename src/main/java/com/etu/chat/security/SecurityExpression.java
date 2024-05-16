package com.etu.chat.security;

import lombok.Getter;

@Getter
public enum SecurityExpression {
    ROOM_INFO("@chatUserService.isCanReadRoom(principal.username, @roomService.getRoom(#id)) || hasRole('ADMIN')");

    private final String value;

    SecurityExpression(String value) {
        this.value = value;
    }
}

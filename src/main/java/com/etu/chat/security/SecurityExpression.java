package com.etu.chat.security;

import lombok.Getter;

@Getter
public enum SecurityExpression {
    ROOM_INFO("@chatUserService.isInRoom(principal.username, @roomService.getRoom(#id))"),
    ROOM_MESSAGE_READ("@chatUserService.hasAuthority(principal.username, @authorityService.getAuthorityForReadRoom(@roomService.getRoom(#roomId)))"),
    ROOM_MESSAGE_WRITE("@chatUserService.hasAuthority(principal.username, @authorityService.getAuthorityForWriteRoom(@roomService.getRoom(#roomId)))"),
    MESSAGE_EDIT("@messageService.canEdit(principal.username, #id) || hasRole('ADMIN')");

    private final String value;

    SecurityExpression(String value) {
        this.value = value;
    }
}

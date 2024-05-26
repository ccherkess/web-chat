package com.etu.chat.security;

import lombok.Getter;

@Getter
public enum SecurityExpression {
    ROOM_INFO("@chatUserService.isInRoom(principal.username, @roomService.getRoom(#id))"),
    ROOM_MESSAGE_READ("hasAuthority(@authorityService.getAuthorityForReadRoom(@roomService.getRoom(#roomId)).name)"),
    ROOM_MESSAGE_WRITE("hasAuthority(@authorityService.getAuthorityForWriteRoom(@roomService.getRoom(#roomId)).name)"),
    MESSAGE_EDIT("@messageService.canEdit(principal.username, #id)");

    private final String value;

    SecurityExpression(String value) {
        this.value = value;
    }
}

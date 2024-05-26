package com.etu.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoomAuthority {
    private boolean canRead;
    private boolean canWrite;
}

package com.etu.chat.dto;

import com.etu.chat.entity.Authority;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserWithAuthority {
    private Long id;
    private String name;
    private List<Authority> authorities;
}

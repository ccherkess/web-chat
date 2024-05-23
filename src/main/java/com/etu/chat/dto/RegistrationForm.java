package com.etu.chat.domain;

import com.etu.chat.entity.ChatUser;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {

    private String username;
    private String password;

    public ChatUser toUser(PasswordEncoder passwordEncoder) {
        return ChatUser.builder()
                .name(username)
                .password(passwordEncoder.encode(password))
                .build();
    }

}

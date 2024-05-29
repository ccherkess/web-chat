package com.etu.chat.valid;

import com.etu.chat.service.ChatUserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class ValidatorProvider {
    private static final ValidatorProvider INSTANCE = new ValidatorProvider();

    @Autowired
    private ChatUserService chatUserService;

    private ValidatorProvider() {
    }

    public static ValidatorProvider getInstance() {
        return INSTANCE;
    }
}

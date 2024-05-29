package com.etu.chat.valid.validator;

import com.etu.chat.repository.ChatUserRepository;
import com.etu.chat.valid.UsernameNotTake;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsernameNotTakeValidator implements ConstraintValidator<UsernameNotTake, String> {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return chatUserRepository.findByName(value).isEmpty();
    }
}
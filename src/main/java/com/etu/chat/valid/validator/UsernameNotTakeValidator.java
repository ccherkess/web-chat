package com.etu.chat.valid.validator;

import com.etu.chat.valid.UsernameNotTake;
import com.etu.chat.valid.ValidatorProvider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameNotTakeValidator implements ConstraintValidator<UsernameNotTake, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ValidatorProvider.getInstance().getChatUserService().find(value).isEmpty();
    }
}
package com.etu.chat.valid;

import com.etu.chat.valid.validator.UsernameNotTakeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= UsernameNotTakeValidator.class)
public @interface UsernameNotTake {
    String message() default "{UsernameNotTake.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

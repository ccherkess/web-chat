package com.etu.chat.controller;

import com.etu.chat.dto.RegistrationForm;
import com.etu.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final ChatUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String getRegisterForm() {
        return "register";
    }

    @PostMapping
    public String registerUser(RegistrationForm form) {
        userRepository.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }

}

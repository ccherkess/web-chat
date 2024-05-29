package com.etu.chat.controller;

import com.etu.chat.entity.Authority;
import com.etu.chat.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    @Value("${spring.profiles.active}")
    private String profile;

    private final ChatUserService chatUserService;

    @GetMapping
    public String getIndex(Model model, @AuthenticationPrincipal User user) {
        HashMap<Object, Object> data = new HashMap<>();

        data.put("username", user.getUsername());
        data.put("isAdmin", chatUserService.hasAuthority(user.getUsername(), Authority.builder().name("ROLE_ADMIN").build()));

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}

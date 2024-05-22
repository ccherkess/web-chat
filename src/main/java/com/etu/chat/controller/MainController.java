package com.etu.chat.controller;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.ChatUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.etu.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.etu.chat.service.ChatUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MainController {
    @Autowired
    private ChatUserService chatUserService;

    @GetMapping("/")
    public String getMainPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        HashMap<Object, Object> data = new HashMap<>();

        data.put("profile", userDetails);
        if (userDetails != null) {
            data.put("isAdmin", chatUserService.hasAuthority(userDetails.getUsername(), Authority.builder().name("ROLE_ADMIN").build()));
        } else {
            data.put("isAdmin", false);
        }
        model.addAttribute("frontendData", data);

        System.out.println("Data sent to frontend: " + data);

        return "index";
    }

}

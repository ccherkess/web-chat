package com.etu.chat.controller;

import com.etu.chat.entity.Authority;
import com.etu.chat.entity.ChatUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.etu.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class MainController {
    @Autowired
    private ChatUserRepository chatUserRepository;

    @GetMapping("/")
    public String getMainPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute(
                "isAdmin",
                userDetails.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
        );
        return "index";
    }

}

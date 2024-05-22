package com.etu.chat.controller;

import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.Room;
import com.etu.chat.entity.json_view.Views;
import com.etu.chat.repository.ChatUserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequestMapping(consumes = "application/json", path = "api/user")

public class UserController {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @GetMapping("/current")
    @JsonView(Views.Low.class)
    public ResponseEntity<ChatUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<ChatUser> user = chatUserRepository.findByName(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @JsonView(Views.Low.class)
    public ResponseEntity<ChatUser> getUserByIs(@PathVariable Long id) {
        Optional<ChatUser> user = chatUserRepository.findById(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getUserNameById(@PathVariable Long id) {
        Optional<ChatUser> user = chatUserRepository.findById(id);

        return user.map(chatUser -> ResponseEntity.ok(chatUser.getName())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    @JsonView(Views.Low.class)
    public Iterable<ChatUser> getAllUsers() {
        return chatUserRepository.findAll();
    }

}

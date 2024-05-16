package com.etu.chat.repository;

import com.etu.chat.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatUserRepository extends CrudRepository<ChatUser, Long> {

    Optional<ChatUser> findByName(String username);

}

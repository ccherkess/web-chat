package com.etu.chat.repository;

import com.etu.chat.entity.ChatUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends CrudRepository<ChatUser, Long> {

    Optional<ChatUser> findByName(String username);

    Optional<ChatUser> findById(long id);

    List<ChatUser> findByIdBeforeOrderByIdDesc(Long startId, Pageable pageable);

    List<ChatUser> findByOrderByIdDesc(Pageable pageable);

}

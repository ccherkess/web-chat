package com.etu.chat.repository;

import com.etu.chat.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByRoomId(Long roomId, Pageable pageable);

}

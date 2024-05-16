package com.etu.chat.config;

import com.etu.chat.entity.Message;
import com.etu.chat.entity.Room;
import com.etu.chat.repository.MessageRepository;
import com.etu.chat.repository.RoomRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    public ApplicationRunner dataLoader(MessageRepository messageRepository, RoomRepository roomRepository) {
        return args -> {
//            Room room = new Room();
//
//            room.setName("test room");
//
//            roomRepository.save(room);
//
//            for (int i = 0; i < 100; i++) {
//                messageRepository.save(
//                        Message.builder()
//                                .roomId(room.getId())
//                                .userId(1L)
//                                .payload("TEST MESSAGE %d".formatted(i))
//                        .build());
//            }
        };
    }
}

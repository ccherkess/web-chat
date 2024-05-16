package com.etu.chat.entity;

import com.etu.chat.entity.json_view.RoomViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(RoomViews.IdName.class)
    private Long id;

    @Column(name = "c_name")
    @JsonView(RoomViews.IdName.class)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "c_room_id", updatable = false, insertable = false)
    private List<Message> messages = new ArrayList<>();
}

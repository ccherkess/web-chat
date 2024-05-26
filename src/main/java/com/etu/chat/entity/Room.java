package com.etu.chat.entity;

import com.etu.chat.entity.json_view.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"messages", "chatUsers"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_room")
public class Room {
    @JsonView(Views.Low.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(Views.Low.class)
    @Column(name = "c_name")
    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "c_room_id", updatable = false, insertable = false)
    private List<Message> messages = new ArrayList<>();

    @JsonView(Views.Medium.class)
    @ManyToMany(cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "t_user_room",
            joinColumns = @JoinColumn(name = "c_room_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "c_user_id", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private List<ChatUser> chatUsers = new ArrayList<>();

    public void addUser(ChatUser user) {
        if (chatUsers == null) {
            chatUsers = new ArrayList<>();
        }
        chatUsers.add(user);
    }
}

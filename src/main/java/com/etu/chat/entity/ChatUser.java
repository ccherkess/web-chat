package com.etu.chat.entity;

import com.etu.chat.entity.json_view.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"})
@Entity
@Table(name = "t_user")
public class ChatUser {
    @JsonView(Views.Low.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(Views.Low.class)
    @Column(name = "c_username")
    private String name;

    @JsonIgnore
    @Column(name = "c_password")
    private String password;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "t_user_authority",
            joinColumns = @JoinColumn(name = "c_user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "c_authority_id", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private List<Authority> authorities = new ArrayList<>();

    @JsonView(Views.High.class)
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "t_user_room",
            joinColumns = @JoinColumn(name = "c_user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "c_room_id", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private List<Room> rooms = new ArrayList<>();
}

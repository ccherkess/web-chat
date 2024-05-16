package com.etu.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_username")
    private String name;

    @Column(name = "c_password")
    private String password;

    @ManyToMany
    @JoinTable(name = "t_user_authority",
        joinColumns = @JoinColumn(name = "c_user_id"),
        inverseJoinColumns = @JoinColumn(name = "c_authority_id")
    )
    private List<Authority> authorities = new ArrayList<>();
}

package com.etu.chat.entity;

import com.etu.chat.entity.json_view.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonView(Views.Low.class)
@Table(name = "t_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_room_id")
    private Long roomId;

    @Column(name = "c_payload")
    private String payload;

    @ManyToOne
    @JoinColumn(name = "c_user_id")
    private ChatUser user;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @CreationTimestamp
    @Column(name = "c_send_at")
    private LocalDateTime sendAt;
}
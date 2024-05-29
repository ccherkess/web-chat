package com.etu.chat.entity;

import com.etu.chat.entity.json_view.Views;
import com.etu.chat.valid.UsernameNotTake;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonView(Views.Low.class)
@Table(name = "t_reqistration_request")
public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 64, message = "Слишком короткое или длинное имя!")
//    @UsernameNotTake(message = "Данное имя уже занято!")
    @Column(name = "c_name")
    private String username;

    @NotNull
    @Size(min = 2, max = 64, message = "Слишком короткий пароль!")
    @Column(name = "c_password")
    private String password;
}

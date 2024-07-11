package com.sparta.trello.domain.auth.entity;

import com.sparta.trello.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.security.auth.message.AuthStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String password;

    @OneToOne(mappedBy = "auth")
    private User user;
}

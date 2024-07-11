package com.sparta.trello.domain.user.entity;

import com.sparta.trello.domain.auth.entity.Auth;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToOne
    @JoinColumn(name = "auth_id")
    private Auth auth;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}

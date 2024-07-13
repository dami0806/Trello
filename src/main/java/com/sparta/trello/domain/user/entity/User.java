package com.sparta.trello.domain.user.entity;

import com.sparta.trello.domain.common.entity.BaseEntity;
import com.sparta.trello.domain.userRole.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();


    @Column
    private String refreshToken;

    public void login() {
        this.userStatus = UserStatus.ACTIVE;
    }

    public void logout() {
        this.userStatus = UserStatus.LOGOUT;
    }

    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public void clearRefreshToken() {
        this.refreshToken = null;
    }
}

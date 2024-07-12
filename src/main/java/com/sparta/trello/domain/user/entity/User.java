package com.sparta.trello.domain.user.entity;

import com.sparta.trello.domain.auth.entity.Auth;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardInvitation;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @OneToOne
    @JoinColumn(name = "auth_id")
    private Auth auth;

    @OneToMany
    @JoinColumn(name =  "boardInvitationId")
    private List<BoardInvitation> boardInvitationList= new ArrayList<>();

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}

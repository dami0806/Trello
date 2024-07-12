package com.sparta.trello.domain.user.entity;

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
    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 이름
    @Column(nullable = false)
    private String username;

    // 사용자 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    // 사용자 역할
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    // refreshToken
    @Column
    private String refreshToken;


    @OneToMany
    @JoinColumn(name =  "boardInvitationId")
    private List<BoardInvitation> boardInvitationList= new ArrayList<>();



    // 로그인 상태 변경
    public void login() {
        this.userStatus = UserStatus.ACTIVE;
    }

    // 로그아웃 (UserStatus 변경)
    public void logout() {
        this.userStatus = UserStatus.LOGOUT; // userStatus를 LOGOUT으로 변경
    }

    // 탈퇴 (UserStatus 변경)
    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    // 토큰 삭제
    public void clearRefreshToken() {
        this.refreshToken = null;
    }

}

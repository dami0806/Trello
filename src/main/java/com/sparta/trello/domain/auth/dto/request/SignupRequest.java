package com.sparta.trello.domain.auth.dto.request;

import com.sparta.trello.domain.user.entity.UserRole;
import com.sparta.trello.domain.user.entity.UserStatus;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;         // 이메일
    private String password;      // 비밀번호
    private String username;      // 이름
    private UserRole userRole;    // 사용자 역할
}
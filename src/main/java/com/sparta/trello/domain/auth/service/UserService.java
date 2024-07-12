package com.sparta.trello.domain.auth.service;

import com.sparta.trello.domain.auth.dto.request.LoginRequest;
import com.sparta.trello.domain.auth.dto.request.SignupRequest;
import com.sparta.trello.domain.auth.dto.response.LoginResponse;
import com.sparta.trello.domain.auth.dto.response.TokenResponseDto;
import com.sparta.trello.domain.user.entity.User;

public interface UserService {
    // 회원가입
    void signup(SignupRequest signupRequest);

    // 로그인
    LoginResponse login(LoginRequest loginRequest);

    // 리프레시 토큰
    TokenResponseDto refresh(String refreshToken);

    // 로그아웃
    void logout(String userId, String accessToken);

    // 탈퇴
    void withdraw(String userId, String password, String accessToken, String refreshToken);

    User getUserById(Long id);
}


package com.sparta.trello.domain.auth.service;

import com.sparta.trello.domain.auth.dto.request.LoginRequest;
import com.sparta.trello.domain.auth.dto.request.SignupRequest;
import com.sparta.trello.domain.auth.dto.response.LoginResponse;
import com.sparta.trello.domain.auth.dto.response.TokenResponseDto;
import com.sparta.trello.domain.user.dto.UserResponse;
import com.sparta.trello.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    // 회원가입
    void signup(SignupRequest signupRequest);

    // 로그인
    LoginResponse login(LoginRequest loginRequest);

    // 리프레시 토큰
    TokenResponseDto refresh(String refreshToken);

    // 로그아웃
    void logout(String email, String accessToken);

    // 탈퇴
    void withdraw(String email, String password, String accessToken);

    User getUserById(Long id);


    User getUserByName(String username);

    User getUserByEmail(String email);


    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersNotInvitedToBoard(Long boardId);

    List<UserResponse> getUsersInvitedToBoard(Long boardId);

    User getUserByNameActive(String username);
}


package com.sparta.trello.domain.auth.controller;

import com.sparta.trello.domain.auth.dto.request.LoginRequestDto;
import com.sparta.trello.domain.auth.dto.request.SignupRequestDto;
import com.sparta.trello.domain.auth.dto.response.LoginResponseDto;
import com.sparta.trello.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + response.getAccessToken());
        headers.add("Refresh-Token", "Bearer " + response.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

}

package com.sparta.trello.domain.auth.controller;

import com.sparta.trello.domain.auth.dto.request.LoginRequest;
import com.sparta.trello.domain.auth.dto.request.RefreshTokenRequest;
import com.sparta.trello.domain.auth.dto.request.SignupRequest;
import com.sparta.trello.domain.auth.dto.response.LoginResponse;
import com.sparta.trello.domain.auth.dto.response.TokenResponseDto;
import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     *
     * @param signupRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    /**
     * 로그인
     *
     * @param loginRequest
     * @return 헤더에 반환
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse tokens = userService.login(loginRequest); // 로그인 시도 및 토큰 생성
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        // 각 토큰을 별도의 헤더에 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("Refresh-Token", refreshToken);

        return new ResponseEntity<>("로그인 성공", headers, HttpStatus.OK);
    }

    /**
     * 로그아웃
     *
     * @param email
     * @param accessToken
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String email, String accessToken) {
        userService.logout(email, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다");
    }

    /**
     * 회원탈퇴
     *
     * @param email
     * @param accessToken
     * @return
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam String email, String password, String accessToken) {
        userService.withdraw(email, password, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴가 완료되었습니다.");
    }

    /**
     * 리프레시 토큰 재발급
     *
     * @param refreshTokenRequest
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponseDto tokenResponseDto = userService.refresh(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(tokenResponseDto);
    }
}

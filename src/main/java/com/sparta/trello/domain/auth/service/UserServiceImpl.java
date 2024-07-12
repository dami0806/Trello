package com.sparta.trello.domain.auth.service;

import com.sparta.trello.domain.auth.dto.request.LoginRequest;
import com.sparta.trello.domain.auth.dto.request.SignupRequest;
import com.sparta.trello.domain.auth.dto.response.LoginResponse;
import com.sparta.trello.domain.auth.dto.response.TokenResponseDto;
import com.sparta.trello.domain.auth.util.JwtUtil;
import com.sparta.trello.domain.user.entity.User;

import com.sparta.trello.domain.user.entity.UserRole;
import com.sparta.trello.domain.user.entity.UserStatus;
import com.sparta.trello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Override
    public void signup(SignupRequest signupRequest) {
        String password = signupRequest.getPassword();
        String email = signupRequest.getEmail();


        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{10,}$")) {
            throw new RuntimeException("비밀번호는 최소 10자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        }


        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("중복된 이메일이 존재합니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(email)
                .password(encodedPassword)
                .userStatus(UserStatus.ACTIVE)
                .userRole(signupRequest.getUserRole())
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        user.updateRefreshToken(refreshToken);
        user.updateUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    /**
     * 로그아웃 메서드
     *
     * @param email
     * @param accessToken
     */
    @Override
    public void logout(String email, String accessToken) {
        // User 찾기
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        // logout
        user.logout();
        user.clearRefreshToken();
        // user 정보 저장
        userRepository.save(user);
    }

    /**
     * 회원 탈퇴 (refreshToken 삭제)
     *
     * @param email
     * @param accessToken
     * @param refreshToken
     */
    @Override
    public void withdraw(String email, String password, String accessToken, String refreshToken) {
        // User 찾기
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        //이미 탈퇴한 회원인지 확인
        if (user.getUserStatus().equals(UserStatus.WITHDRAWN)) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다");
        }
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 탈퇴
        user.withdraw();
        user.clearRefreshToken();
        // user 정보 저장
        userRepository.save(user);
    }

    /**
     * 리프레시 토큰으로 토큰 재발급
     *
     * @param refreshToken
     * @return TokenResponseDto
     */
    @Override
    public TokenResponseDto refresh(String refreshToken) {
        String email = jwtUtil.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("잘못된 리프레시 토큰입니다.");
        }

        String newAccessToken = jwtUtil.createAccessToken(email);
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

//    @Override
//    public User getCurrentAdmin() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//        if (user.getUserRole() != UserRole.MANAGER) {
//            throw new IllegalArgumentException("Current user is not an admin");
//        }
//        return user;
//    }
//
}
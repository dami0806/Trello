package com.sparta.trello.domain.auth.service;

import com.sparta.trello.domain.auth.dto.request.LoginRequest;
import com.sparta.trello.domain.auth.dto.request.SignupRequest;
import com.sparta.trello.domain.auth.dto.response.LoginResponse;
import com.sparta.trello.domain.auth.dto.response.TokenResponseDto;
import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.auth.util.JwtUtil;
import com.sparta.trello.domain.boardInvitaion.service.BoardInvitationService;
import com.sparta.trello.domain.role.entity.Role;
import com.sparta.trello.domain.user.dto.UserResponse;
import com.sparta.trello.domain.user.entity.User;

import com.sparta.trello.domain.user.entity.UserStatus;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.domain.userRole.entity.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    @Override
    public void signup(SignupRequest signupRequest) {
        String password = signupRequest.getPassword();
        String email = signupRequest.getEmail();

        inValidPassword(password);
        String encodedPassword = passwordEncoder.encode(password);
        inValidEamil(email);

        User user = User.builder()
                .username(signupRequest.getUsername())
                .email(email)
                .password(encodedPassword)
                .userStatus(UserStatus.LOGOUT)
                .build();
        userRepository.save(user);

        userAddUserRole(user);
    }

    @Transactional
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = getUserByEmail(loginRequest.getEmail());


        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        user.updateRefreshToken(refreshToken);
        user.login();
        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    /**
     * 로그아웃 메서드
     *
     * @param email
     * @param accessToken
     */
    @Transactional
    @Override
    public void logout(String email, String accessToken) {
        // User 찾기
        User user = getUserByEmail(email);
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
     */
    @Transactional
    @Override
    public void withdraw(String email, String password, String accessToken) {
        // User 찾기
        User user = getUserByEmail(email);

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
    @Transactional
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

    @Override
    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getUsersNotInvitedToBoard(Long boardId) {
        return userRepository.getUsersNotInvitedToBoard(boardId).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getUsersInvitedToBoard(Long boardId) {
        return userRepository.getUsersInvitedToBoard(boardId).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    private void inValidPassword(String password) {
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{10,}$")) {
            throw new IllegalArgumentException("비밀번호는 최소 10자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        }
    }
    private void inValidEamil(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }
    }

    private void userAddUserRole(User user) {
        Role userRole = userRepository.getRoleByName("ROLE_USER");
        if (userRole == null) {
            throw new IllegalArgumentException("기본 사용자 역할을 찾을 수 없습니다.");
        }

        UserRole userRoleEntity = UserRole.builder()
                .user(user)
                .role(userRole)
                .build();

        user.getRoles().add(userRoleEntity);
        userRepository.save(user);
    }


    @Override
    public User getUserByNameActive(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UnauthorizedException("유저를 찾을수 없습니다."));
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("활성화된 사용자가 아닙니다.");
        }
        return user;
    }
}
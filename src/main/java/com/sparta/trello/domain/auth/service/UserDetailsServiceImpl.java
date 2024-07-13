package com.sparta.trello.domain.auth.service;

import com.sparta.trello.domain.user.dto.UserResponse;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * loadUserByUsername: 유저 찾기
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toString())
                .build();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getUsersNotInvitedToBoard(Long boardId) {
        List<User> invitedUsers = userRepository.getUsersNotInvitedToBoard(boardId);
        List<User> allUsers = userRepository.findAll();
        allUsers.removeAll(invitedUsers);
        return allUsers.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}

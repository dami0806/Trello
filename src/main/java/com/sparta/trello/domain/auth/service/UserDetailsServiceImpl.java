package com.sparta.trello.domain.auth.service;

import com.sparta.trello.domain.auth.entity.Auth;
import com.sparta.trello.domain.auth.repository.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthRepository authRepository;

    public UserDetailsServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * loadUserByUsername: 유저 찾기
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Auth> authOptional = authRepository.findByName(username);

        if (authOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Auth auth = authOptional.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(auth.getName())
                .password(auth.getPassword())
                .build();
    }
}


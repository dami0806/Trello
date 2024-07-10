package com.sparta.trello.domain.user.service;

import com.sparta.trello.domain.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void save(User user);

}

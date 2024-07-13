package com.sparta.trello.domain.user.dto;
import com.sparta.trello.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String username;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}
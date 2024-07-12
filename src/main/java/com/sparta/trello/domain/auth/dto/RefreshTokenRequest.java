package com.sparta.trello.domain.auth.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}
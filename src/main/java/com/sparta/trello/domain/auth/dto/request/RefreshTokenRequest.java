package com.sparta.trello.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}

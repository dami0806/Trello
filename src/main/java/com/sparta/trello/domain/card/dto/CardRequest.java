package com.sparta.trello.domain.card.dto;

import lombok.Getter;

@Getter
public class CardRequest {
    private String title;
    private String content;
    private Long columnId;
}

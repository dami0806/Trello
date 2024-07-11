package com.sparta.trello.domain.card.dto;

import java.time.LocalDateTime;

public class CardResponse {
    private Long id;
    private String title;
    private String content;
    private String manager;
    private int position;

    private Long columnId;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}

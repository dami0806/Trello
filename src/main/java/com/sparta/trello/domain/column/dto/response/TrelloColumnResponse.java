package com.sparta.trello.domain.column.dto.response;

import com.sparta.trello.domain.card.dto.CardResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TrelloColumnResponse {
    private Long id;
    private String title;
    private int position;
    private String status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<CardResponse> cards;
}

package com.sparta.trello.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter //Lombok
@AllArgsConstructor
public class CardResponse {
    private Long id;
    private String title;
    private String description;
    private String manager;
    private int position;

    private Long trelloColumnId;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}

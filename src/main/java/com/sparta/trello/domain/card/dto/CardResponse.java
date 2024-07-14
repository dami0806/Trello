package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.comment.dto.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter //Lombok
@AllArgsConstructor
public class CardResponse {
    private Long id;
    private String title;
    private String description;
    private String manager;

    private Long trelloColumnId;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}

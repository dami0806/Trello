package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.comment.dto.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter //Lombok
@AllArgsConstructor
public class CardDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String manager;
    private int position;

    private Long trelloColumnId;
    private List<CommentResponse> comments;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void updateComments(List<CommentResponse> commentResponses) {
        this.comments = commentResponses;
    }
}


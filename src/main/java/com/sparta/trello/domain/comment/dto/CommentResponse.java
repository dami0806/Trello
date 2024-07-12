package com.sparta.trello.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
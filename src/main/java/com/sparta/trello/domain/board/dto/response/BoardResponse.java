package com.sparta.trello.domain.board.dto.response;

import com.sparta.trello.domain.board.entity.BoardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponse {

    private long boardId;
    private String boardName;
    private String description;
    private BoardStatus boardStatus;
}

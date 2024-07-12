package com.sparta.trello.domain.board.dto.response;

import lombok.Getter;

@Getter
public class BoardResponse {

    private String boardName;
    private String description;

    public BoardResponse(String boardName, String description) {
        this.boardName = boardName;
        this.description = description;
    }
}

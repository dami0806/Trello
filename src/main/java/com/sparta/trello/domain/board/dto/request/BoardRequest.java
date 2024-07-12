package com.sparta.trello.domain.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardRequest {

    private String boardName;
    private String description;
}

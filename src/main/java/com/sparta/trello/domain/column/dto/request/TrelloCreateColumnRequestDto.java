package com.sparta.trello.domain.column.dto.request;

import lombok.Getter;

@Getter
public record TrelloCreateColumnRequestDto(String columns_title, Long boardId) {
}

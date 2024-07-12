package com.sparta.trello.domain.column.dto.request;

public record TrelloCreateColumnRequestDto(String columns_title, Long boardId, int newPosition) {
}

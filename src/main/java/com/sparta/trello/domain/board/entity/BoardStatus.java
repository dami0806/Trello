package com.sparta.trello.domain.board.entity;

public enum BoardStatus {
    ACTIVE("정상"),
    DELETE("삭제");

    private final String boardStatus;

    BoardStatus(String boardStatus) {this.boardStatus = boardStatus;}
}

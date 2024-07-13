package com.sparta.trello.domain.board.repository;

import com.sparta.trello.domain.user.entity.User;

public interface BoardRepositoryCustom {
    boolean isBoardManager(Long boardId, User user);

    boolean isBoardMember(Long boardId, User user);
}

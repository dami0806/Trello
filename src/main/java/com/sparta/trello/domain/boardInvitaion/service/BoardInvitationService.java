package com.sparta.trello.domain.boardInvitaion.service;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.user.entity.User;

import java.util.List;

public interface BoardInvitationService {
    void inviteUserToBoard(User user, Board board, String role);

    List<User> getUsersByBoardId(Long boardId);
}

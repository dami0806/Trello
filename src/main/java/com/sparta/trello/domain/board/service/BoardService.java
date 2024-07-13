package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.auth.service.UserDetailsServiceImpl;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.boardInvitaion.dto.BoardInvitationRequest;
import com.sparta.trello.domain.common.util.SecurityUtils;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BoardService {
    public BoardResponse createBoard(BoardRequest boardRequest, String username);

    public boolean isBoardManager(Long boardId, User user);

    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest, String username);

    public void deleteBoard(Long boardId, String username);

    Board findBoardById(Long boardId);

    void inviteUserToBoard(Long boardId, BoardInvitationRequest invitationRequest, String username);

    boolean isBoardMember(Long boardId, User user);
    boolean isBoardMemberOrManager(Long boardId, String username);
}
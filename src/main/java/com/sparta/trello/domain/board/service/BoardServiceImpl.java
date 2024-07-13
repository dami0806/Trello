package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.board.mapper.BoardMapper;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.boardInvitaion.dto.BoardInvitationRequest;
import com.sparta.trello.domain.boardInvitaion.service.BoardInvitationService;
import com.sparta.trello.domain.role.entity.Role;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardInvitationService invitationService;
    private final UserService userService;
    private final BoardMapper mapper;

    // 보드 생성
    @Override
    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest, String username) {
        User user = userService.getUserByName(username);

        Board board = Board.builder()
                .boardName(boardRequest.getBoardName())
                .description(boardRequest.getDescription())
                .boardStatus(BoardStatus.ACTIVE)
                .user(user)
                .build();

        board = boardRepository.save(board);

        // 보드 생성자를 매니저로 초대
        invitationService.inviteUserToBoard(user, board, "MANAGER");

        return new BoardResponse(board.getBoardId(), board.getBoardName(), board.getDescription(), board.getBoardStatus());
    }


    // 보드 수정
    @Transactional
    @Override
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest, String username) {
        User user = getCurrentUser(username);
        Board board = findBoardById(boardId);

        checkBoardManager(boardId, user);

        board.update(boardRequest.getBoardName(), boardRequest.getDescription());
        boardRepository.save(board);

        return mapper.toBoardResponse(board);
    }

    //보드 삭제
    @Transactional
    @Override
    public void deleteBoard(Long boardId, String username) {
        User user = getCurrentUser(username);
        Board board = findBoardById(boardId);

        checkBoardManager(boardId, user);

        board.softDelete();
        boardRepository.save(board);
    }

    // / 현재 로그인된 사용자 가져오기
    private User getCurrentUser(String username) {
        return userService.getUserByNameActive(username);
    }

    //보드 Manager인지
    private void checkBoardManager(Long boardId, User user) {
        if (!isBoardManager(boardId, user)) {
            throw new IllegalArgumentException("해당 보드 작업에 권한이 없습니다.");
        }
    }

    @Override
    public boolean isBoardManager(Long boardId, User user) {
        return boardRepository.isBoardManager(boardId, user);
    }

    // 보드 찾음
    @Override
    @Transactional(readOnly = true)
    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void inviteUserToBoard(Long boardId,BoardInvitationRequest invitationRequest, String username) {
        User currentUser = getCurrentUser(username);
        Board board = findBoardById(boardId);

        checkBoardManager(boardId, currentUser);

        User userToInvite = userService.getUserByEmail(invitationRequest.getEmail());

        invitationService.inviteUserToBoard(userToInvite, board, invitationRequest.getRole()); // Manager, Member둘다 가능
    }
}


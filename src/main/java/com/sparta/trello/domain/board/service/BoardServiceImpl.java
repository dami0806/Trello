package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.boardInvitaion.service.BoardInvitationService;
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

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
    }

    public boolean isBoardOwner(Long boardId, User user) {
        Board board = findBoardById(boardId);
        return board.getUser().equals(user);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = findBoardById(boardId);
        board.softDelete();
        boardRepository.save(board);
    }

    // 보드 수정 메서드를 필요한 경우 추가
    // @Transactional
    // public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
    //     if (boardRequest.getBoardName() == null || boardRequest.getDescription() == null) {
    //         throw new IllegalArgumentException("수정 할 보드 이름, 한 줄 설명을 입력해주세요.");
    //     }
    //     Board board = findBoardById(boardId);
    //     board.update(boardRequest.getBoardName(), boardRequest.getDescription());
    //     return new BoardResponse(board.getBoardId(), board.getBoardName(), board.getDescription(), board.getBoardStatus());
    // }
}

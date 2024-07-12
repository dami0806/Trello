package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.common.util.SecurityUtils;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    // 보드 생성
    @Override
    public BoardResponse createBoard(BoardRequest boardRequest, String username) {
        User user = userService.getUserByName(username);

        Board board = Board.builder()
                .boardName(boardRequest.getBoardName())
                .description(boardRequest.getDescription())
                .boardStatus(BoardStatus.ACTIVE)
                .user(user)
                .build();

        board = boardRepository.save(board);

        return new BoardResponse(board.getBoardId(), board.getBoardName(), board.getDescription(), board.getBoardStatus());
    }

//    @Transactional
//    // 보드 수정
//    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
//
//        if (boardRequest.getBoardName() == null || boardRequest.getDescription() == null) {
//            throw new IllegalArgumentException("수정 할 보드 이름, 한 줄 설명을 입력해주세요.");
//        }
//
//        Board board = findBoardById(boardId);
//        board.update(boardRequest.getBoardName(), boardRequest.getDescription());
//
//        return new BoardResponse(board.getBoardName(), board.getDescription());
//    }

    public boolean isBoardOwner(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));
        return board.getUser().equals(user);
    }

    @Transactional
    // 보드 삭제
    public void deleteBoard(Long boardId) {

        Board board = findBoardById(boardId);
        board.softDelete();
        boardRepository.save(board);
    }

    private Board findBoardById(Long BoardId) {

        return boardRepository.findById(BoardId).orElseThrow(
                () -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다.")
        );
    }
}

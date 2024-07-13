package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 보드 생성
    public BoardResponse createBoard(BoardRequest boardRequest, User user) {
        if (boardRequest.getBoardName() == null || boardRequest.getDescription() == null) {
            throw new IllegalArgumentException("보드 이름, 한 줄 설명을 입력해주세요.");
        }

        String boardName = boardRequest.getBoardName();
        String description = boardRequest.getDescription();

        Board board = new Board(boardName, description, BoardStatus.ACTIVE, user);
        boardRepository.save(board);

        return new BoardResponse(board.getBoardName(), board.getDescription());
    }

    @Transactional
    // 보드 수정
    public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest) {
        if (boardRequest.getBoardName() == null || boardRequest.getDescription() == null) {
            throw new IllegalArgumentException("수정 할 보드 이름, 한 줄 설명을 입력해주세요.");
        }

        Board board = findBoardById(boardId);
        board.update(boardRequest.getBoardName(), boardRequest.getDescription());

        return new BoardResponse(board.getBoardName(), board.getDescription());
    }

    @Transactional
    // 보드 삭제
    public void deleteBoard(Long boardId) {
        Board board = findBoardById(boardId);
        board.softDelete();
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    // 사용자와 관련된 모든 보드 가져오기
    public List<BoardResponse> getBoards(User user) {
        List<Board> boards = boardRepository.findByUserAndBoardStatus(user, BoardStatus.ACTIVE);
        return boards.stream()
            .map(board -> new BoardResponse(board.getBoardName(), board.getDescription()))
            .collect(Collectors.toList());
    }

    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
            () -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다.")
        );
    }
}
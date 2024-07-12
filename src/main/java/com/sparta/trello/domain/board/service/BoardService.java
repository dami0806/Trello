package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponse createBoard(BoardRequest boardRequest) {

        if (boardRequest.getBoardName() == null || boardRequest.getDescription() == null) {
            throw new IllegalArgumentException("보드 이름, 한 줄 설명을 입력해주세요.");
        }

        String boardName = boardRequest.getBoardName();
        String description = boardRequest.getDescription();

        Board board = new Board(boardName, description, BoardStatus.ACTIVE);
        boardRepository.save(board);

        return new BoardResponse(board.getBoardName(), board.getDescription());
    }
}

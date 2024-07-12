package com.sparta.trello.domain.board.controller;

import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest) {

        BoardResponse response = boardService.createBoard(boardRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId,
                                                     @RequestBody BoardRequest boardRequest) {

        BoardResponse response = boardService.updateBoard(boardId, boardRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

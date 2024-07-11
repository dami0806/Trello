package com.sparta.trello.domain.board.controller;

import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest) {

        BoardResponse response = boardService.createBoard(boardRequest);
        System.out.println(response.getBoardName());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

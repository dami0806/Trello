package com.sparta.trello.domain.board.controller;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/boards")
public class BoardController {

        private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        BoardResponse response = boardService.createBoard(boardRequest, username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//        @PutMapping("/{boardId}")
//        public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId,
//                                                         @RequestBody BoardRequest boardRequest) {
//            BoardResponse response = boardService.updateBoard(boardId, boardRequest);
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        }

//        @DeleteMapping("/{boardId}")
//        public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
//            boardService.deleteBoard(boardId);
//            return new ResponseEntity<>("보드가 삭제되었습니다.", HttpStatus.OK);
//        }

}

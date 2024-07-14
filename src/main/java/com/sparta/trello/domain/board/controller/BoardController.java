package com.sparta.trello.domain.board.controller;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.boardInvitaion.dto.BoardInvitationRequest;
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
    //수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest boardRequest,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest, username);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse);
    }


    //삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUsername());
        return new ResponseEntity<>("삭제성공", HttpStatus.OK);
    }

    //초대
    @PostMapping("/invite/{boardId}")
    public ResponseEntity<String> inviteUserToBoard(@PathVariable Long boardId,
                                                    @RequestBody BoardInvitationRequest invitationRequest,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        boardService.inviteUserToBoard(boardId, invitationRequest, userDetails.getUsername());
        return new ResponseEntity<>("초대 성공", HttpStatus.OK);
    }


}

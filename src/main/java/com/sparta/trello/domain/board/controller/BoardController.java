package com.sparta.trello.domain.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.board.dto.request.BoardRequest;
import com.sparta.trello.domain.board.dto.response.BoardResponse;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    private User validateUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증이 필요합니다.");
        }

        return userRepository.findByName(userDetails.getUsername())
            .orElseThrow(() -> new UnauthorizedException("사용자를 찾을 수 없습니다."));
    }

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {
        User user = validateUserDetails(userDetails);
        BoardResponse response = boardService.createBoard(boardRequest, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId,
        @RequestBody BoardRequest boardRequest,
        @AuthenticationPrincipal UserDetails userDetails) {
        validateUserDetails(userDetails);
        BoardResponse response = boardService.updateBoard(boardId, boardRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId,
        @AuthenticationPrincipal UserDetails userDetails) {
        validateUserDetails(userDetails);
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards(@AuthenticationPrincipal UserDetails userDetails) {
        User user = validateUserDetails(userDetails);
        List<BoardResponse> response = boardService.getBoards(user);
        return ResponseEntity.ok(response);
    }
}
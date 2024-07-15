package com.sparta.trello.domain.comment.controller;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.domain.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/columns/{columnId}/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long cardId,
                                                         @PathVariable Long boardId,
                                                         @RequestBody CommentRequest commentRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        checkAuthAndRole(boardId, userDetails);
        CommentResponse commentResponse = commentService.createComment(cardId, commentRequest, username);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @PathVariable Long boardId,
                                                         @RequestBody CommentRequest commentRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        checkAuthAndRole(boardId, userDetails);
        String username = userDetails.getUsername();
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest, username);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @PathVariable Long boardId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        checkAuthAndRole(boardId, userDetails);
        String username = userDetails.getUsername();
        commentService.deleteComment(commentId,username);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    // 권한 검증
    private void checkBoardMemberOrManager(Long boardId, String username) {
        if (!boardService.isBoardMemberOrManager(boardId, username)) {
            throw new UnauthorizedException("해당 보드에 카드를 생성할 권한이 없습니다.");
        }
    }
    private void checkAuthAndRole(Long boardId,  UserDetails userDetails) {
        String username = userDetails.getUsername();
        SecurityUtils.validdateUserDetails(userDetails);
        checkBoardMemberOrManager(boardId, username);
    }
}

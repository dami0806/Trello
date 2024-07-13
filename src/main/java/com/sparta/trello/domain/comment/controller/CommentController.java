package com.sparta.trello.domain.comment.controller;

import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.service.CommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{cardId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long cardId,
                                                         @RequestBody CommentRequest commentRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        CommentResponse commentResponse = commentService.createComment(cardId, commentRequest, username);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody CommentRequest commentRequest,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest, username);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        commentService.deleteComment(commentId,username);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

//    @GetMapping("/card/{cardId}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Page<CommentResponse>> getCommentsByCardId(@PathVariable Long cardId, Pageable pageable) {
//        Page<CommentResponse> comments = commentService.getCommentsByCardId(cardId, pageable);
//        return new ResponseEntity<>(comments, HttpStatus.OK);
//    }
}

package com.sparta.trello.domain.comment.service;

import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(Long cardId, CommentRequest commentRequest);
    CommentResponse updateComment(Long commentId, CommentRequest commentRequest);
    void deleteComment(Long commentId);
}
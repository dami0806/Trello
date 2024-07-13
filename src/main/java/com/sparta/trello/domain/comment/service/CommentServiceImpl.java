package com.sparta.trello.domain.comment.service;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.entity.CommentStatus;
import com.sparta.trello.domain.comment.exception.CommentNotFoundException;
import com.sparta.trello.domain.comment.mapper.CommentMapper;
import com.sparta.trello.domain.comment.repository.CommentRepository;
import com.sparta.trello.domain.common.util.SecurityUtils;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CardService cardService;
    private final CommentMapper commentMapper;
    private final UserService userService;

    @Override
    public CommentResponse createComment(Long cardId, CommentRequest commentRequest,String username) {
        Card card = cardService.findCard(cardId);
        User user = userService.getUserByName(username);

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .user(user)
                .card(card)
                .build();
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest,String username) {
        Comment comment = findComment(commentId);
        validateCommentOwner(comment,username);
        comment.update(commentRequest.getContent());
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public void deleteComment(Long commentId,String username) {
        Comment comment = findComment(commentId);
        validateCommentOwner(comment,username);
        comment.softDelete();
        commentRepository.save(comment);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을수 없습니다."));
    }

    private void validateCommentOwner(Comment comment,String username) {
        User user = userService.getUserByName(username);
        if (!comment.getUser().equals(user)) {
            throw new SecurityException("댓글의 작성자만 가능한 기능입니다.");
        }
    }
}

package com.sparta.trello.domain.card.repository;
import com.sparta.trello.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {
    Page<Comment> findCommentsByCardId(Long cardId, Pageable pageable);
}
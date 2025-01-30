package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.comment.entity.Comment;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 잠금
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Card findByIdWithLock(Long id);

    @Query("SELECT c FROM Card c WHERE c.trelloColumn.id = :columnId ORDER BY c.id DESC LIMIT 1")
    Card findLastCardByColumnId(@Param("columnId") Long columnId);

}

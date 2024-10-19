package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 잠금
    @Query("SELECT c FROM Card c WHERE c.id = :id")
    Card findByIdWithLock(Long id);
}

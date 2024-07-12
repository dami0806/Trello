package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;


public interface CardService {
    CardResponse createCard(CardRequest cardRequest, String username);
    CardResponse updateCard(Long cardId, CardRequest cardRequest);
    void updateCardPosition(Long cardId, int newPosition, Long newColumnId);
    void deleteCard(Long cardId);
//    // 카드 상세보기
//    CardResponse getCardById(Long cardId);
    Card findCard(Long cardId);
    CardResponse getCardById(Long cardId, Pageable pageable);
}

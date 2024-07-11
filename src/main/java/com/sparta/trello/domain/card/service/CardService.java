package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.user.entity.User;
import org.springframework.web.bind.annotation.RestController;


public interface CardService {
    CardResponse createCard(CardRequest cardRequest, User user);
    CardResponse updateCard(Long cardId, CardRequest cardRequest, User user);
    void updateCardPosition(Long cardId, int newPosition, Long newColumnId, User user);
    void deleteCard(Long cardId, User user);
    // 카드 상세보기
    CardResponse getCardById(Long cardId);
}

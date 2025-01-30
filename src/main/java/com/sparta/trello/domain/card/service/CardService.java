package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.card.dto.CardDetailResponse;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;


public interface CardService {
   // CardResponse createCard(Long columnId,CardRequest cardRequest, String username);
    CardResponse createCard(Long columnId, CardRequest cardRequest, Long targetPrevCardId, String username);
    CardResponse updateCard(Long columnId,Long cardId, CardRequest cardRequest, String username);

    void updateCardPosition(Long cardId, Long targetPrevCardId, Long newColumnId);
    void deleteCard(Long cardId, String username);

    Card findCard(Long cardId);
    CardResponse getCardById(Long cardId, Pageable pageable);
    CardDetailResponse getCardDetailById(Long cardId, Pageable pageable);
}

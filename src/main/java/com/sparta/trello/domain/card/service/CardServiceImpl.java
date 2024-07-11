package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.exception.DatabaseAccessException;
import com.sparta.trello.domain.card.mapper.CardMapper;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.column.service.TrelloColumnService;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final TrelloColumnService trelloColumnService;
    private final CardMapper cardMapper;

    //card 생성
    @Override
    public CardResponse createCard(CardRequest cardRequest, User user) {
        TrelloColumn trelloColumn = findTrelloColumn(cardRequest.getTrelloColumnId());

        Card card = Card.builder()
                .title(cardRequest.getTitle())
                .description(cardRequest.getDescription())
                .trelloColumn(trelloColumn)
                .manager(user)
                .position(getPosition(cardRequest.getTrelloColumnId()))
                .build();
        saveCard(card);
        return cardMapper.toCardResponse(card);
    }

    //card 수정
    @Override
    public CardResponse updateCard(Long cardId, CardRequest cardRequest, User user) {
        Card card = findCard(cardId);
        validateCardOwner(card, user);
        card.update(cardRequest.getTitle(), cardRequest.getDescription());
        saveCard(card);
        return cardMapper.toCardResponse(card);
    }

    // card 위치 수정
    @Override
    public void updateCardPosition(Long cardId, int newPosition, Long newColumnId, User user) {

        Card card = findCard(cardId);
        validateCardOwner(card, user);

        TrelloColumn newColumn = findTrelloColumn(newColumnId);

        card.updatePosition(newPosition);
        card.updateColumn(newColumn);
        saveCard(card);
    }

    // card 삭제
    @Override
    public void deleteCard(Long cardId, User user) {
        Card card = findCard(cardId);
        validateCardOwner(card, user);
        card.softDelete();
        saveCard(card);
    }


    // card 상세 보기 - 댓글까지 모두
    @Override
    public CardResponse getCardById(Long cardId) {
        Card card = findCard(cardId);
        return cardMapper.toCardResponse(card);
    }

    // cardRepository save
    private void saveCard(Card card) {
        try {
            cardRepository.save(card);
        } catch (DatabaseAccessException e) {
            throw new DatabaseAccessException("Card 데이터 베이스 접근을 실패했습니다.");
        }
    }

    private Card findCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new DatabaseAccessException("Card 데이터가 없습니다."));
        return card;
    }

    // new position은 마지막 컬럼
    private int getPosition(Long trelloColumnId) {
        TrelloColumn trelloColumn = findTrelloColumn(trelloColumnId);
        int getPosition = trelloColumn.getCards().size();
        return getPosition;
    }

    // trelloColumn찾기
    private TrelloColumn findTrelloColumn(Long trelloColumnId) {
        TrelloColumn trelloColumn = trelloColumnService.findById(trelloColumnId);
        return trelloColumn;
    }


    private void validateCardOwner(Card card, User user) {
        if (!card.getManager().equals(user)) {
            throw new SecurityException("카드의 작성자만 가능한 기능입니다.");
        }
    }
}


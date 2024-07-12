package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.exception.DatabaseAccessException;
import com.sparta.trello.domain.card.mapper.CardMapper;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.column.service.TrelloColumnService;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.mapper.CommentMapper;
import com.sparta.trello.domain.common.util.SecurityUtils;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final TrelloColumnService trelloColumnService;
    private final CardMapper cardMapper;
    private final CommentMapper commentMapper;

    //card 생성
    @Override
    public CardResponse createCard(CardRequest cardRequest, String username) {
        TrelloColumn trelloColumn = findTrelloColumn(cardRequest.getTrelloColumnId());

        Card card = Card.builder()
                .title(cardRequest.getTitle())
                .description(cardRequest.getDescription())
                .trelloColumn(trelloColumn)
                .position(getPosition(cardRequest.getTrelloColumnId()))
                .build();
        saveCard(card);
        return cardMapper.toCardResponse(card);
    }

    //card 수정
    @Override
    public CardResponse updateCard(Long cardId, CardRequest cardRequest) {
        Card card = findCard(cardId);
        validateCardOwner(card);
        card.update(cardRequest.getTitle(), cardRequest.getDescription());
        saveCard(card);
        return cardMapper.toCardResponse(card);
    }

    // card 위치 수정
    @Override
    public void updateCardPosition(Long cardId, int newPosition, Long newColumnId) {

        Card card = findCard(cardId);
        validateCardOwner(card);

        TrelloColumn newColumn = findTrelloColumn(newColumnId);

        card.updatePosition(newPosition);
        card.updateColumn(newColumn);
        saveCard(card);
    }

    // card 삭제
    @Override
    public void deleteCard(Long cardId) {
        Card card = findCard(cardId);
        validateCardOwner(card);
        card.softDelete();
        saveCard(card);
    }


    // card 상세 보기 - 댓글까지 모두
    @Override
    public CardResponse getCardById(Long cardId, Pageable pageable) {
        Card card = findCard(cardId);
        Page<Comment> comments = cardRepository.findCommentsByCardId(cardId, pageable);
        List<CommentResponse> commentResponses = commentMapper.toCommentResponseList(comments.getContent());
        CardResponse cardResponse = cardMapper.toCardResponse(card);
        cardResponse.updateComments(commentResponses);
        return cardResponse;
    }


    // cardRepository save
    private void saveCard(Card card) {
        try {
            cardRepository.save(card);
        } catch (DatabaseAccessException e) {
            throw new DatabaseAccessException("Card 데이터 베이스 접근을 실패했습니다.");
        }
    }


    @Override
    public Card findCard(Long cardId) {
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

    private void validateCardOwner(Card card) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (!card.getManager().equals(currentUser)) {
            throw new SecurityException("카드의 작성자만 가능한 기능입니다.");
        }
    }
}


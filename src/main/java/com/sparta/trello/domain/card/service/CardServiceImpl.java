package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.auth.service.UserService;
import com.sparta.trello.domain.card.dto.CardDetailResponse;
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
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    private final TrelloColumnService trelloColumnService;
    private final UserService userService;

    private final CardMapper cardMapper;
    private final CommentMapper commentMapper;

    //card 생성
    @Transactional
    @Override
    public CardResponse createCard(Long columnId, CardRequest cardRequest, Long targetPrevCardId, String username) {
        User user = userService.getUserByName(username);
        TrelloColumn column = trelloColumnService.findById(columnId);

        // **삽입 위치 찾기**
        Card prevCard = targetPrevCardId != null ? findCard(targetPrevCardId) : null;

        // ❗ `targetPrevCardId`가 없으면 컬럼에서 가장 마지막 카드를 가져오기
        if (prevCard == null) {
            prevCard = cardRepository.findLastCardByColumnId(columnId);
        }

        Card nextCard = prevCard != null ? prevCard.getNextCard() : null;

        // **새 카드 생성**
        Card card = Card.builder()
                .manager(user)
                .title(cardRequest.getTitle())
                .description(cardRequest.getDescription())
                .trelloColumn(column)
                .prevCard(prevCard)
                .nextCard(nextCard)
                .build();

        // **먼저 카드 저장**
        cardRepository.save(card);

        // **Linked List 연결 업데이트**
        if (prevCard != null) {
            prevCard.setNextCard(card);
            cardRepository.save(prevCard);
        }

        if (nextCard != null) {
            nextCard.setPrevCard(card);
            cardRepository.save(nextCard);
        }

        return cardMapper.toCardResponse(card);
    }


    //card 수정
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 5)
    @Override
    public CardResponse updateCard(Long columnId, Long cardId, CardRequest cardRequest, String username) {
        Card card = findCard(cardId);

        validateCardOwner(card, username);

        card.update(cardRequest.getTitle(), cardRequest.getDescription());
        saveCard(card);
        return cardMapper.toCardResponse(card);
    }

    // card 위치 수정
    @Transactional
    @Override
    public void updateCardPosition(Long cardId, Long targetPrevCardId, Long newColumnId) {
        Card card = findCard(cardId);
        TrelloColumn newColumn = trelloColumnService.findById(newColumnId);

        // 기존 컬럼에서 카드 제거 (기존 prev, next 카드 연결 복구)
        if (card.getPrevCard() != null) {
            card.getPrevCard().setNextCard(card.getNextCard());
            cardRepository.save(card.getPrevCard());
        }
        if (card.getNextCard() != null) {
            card.getNextCard().setPrevCard(card.getPrevCard());
            cardRepository.save(card.getNextCard());
        }

        // 새로운 위치에서 prevCard, nextCard 찾기
        Card prevCard = targetPrevCardId != null ? findCard(targetPrevCardId) : null;
        Card nextCard = prevCard != null ? prevCard.getNextCard() : null;

        // 새 위치에서 카드 연결
        card.setPrevCard(prevCard);
        card.setNextCard(nextCard);
        card.updateColumn(newColumn);

        // **카드 먼저 저장**
        cardRepository.save(card);

        if (prevCard != null) {
            prevCard.setNextCard(card);
            cardRepository.save(prevCard);
        }
        if (nextCard != null) {
            nextCard.setPrevCard(card);
            cardRepository.save(nextCard);
        }
    }


    // card 삭제
    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 5)
    @Override
    public void deleteCard(Long cardId, String username) {
        Card card = findCard(cardId);
        validateCardOwner(card, username);
        card.softDelete();
        saveCard(card);
    }

    // card 보기 - 댓글까지 모두
    @Transactional
    @Override
    public CardResponse getCardById(Long cardId, Pageable pageable) {
        Card card = findCard(cardId);
        Page<Comment> comments = cardRepository.findCommentsByCardId(cardId, pageable);
        List<CommentResponse> commentResponses = commentMapper.toCommentResponseList(comments.getContent());
        CardResponse cardResponse = cardMapper.toCardResponse(card);
        return cardResponse;
    }

    // card 상세 보기 - 댓글까지 모두
    @Transactional
    @Override
    public CardDetailResponse getCardDetailById(Long cardId, Pageable pageable) {
        Card card = findCard(cardId);

        Page<Comment> comments = cardRepository.findCommentsByCardId(cardId, pageable);
        List<CommentResponse> commentResponses = commentMapper.toCommentResponseList(comments.getContent());

        CardDetailResponse cardResponse = cardMapper.toCardDetailResponse(card);
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
        Card card = cardRepository.findByIdWithLock(cardId) ;
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

    private void validateCardOwner(Card card, String username) {
        User currentUser = userService.getUserByName(username);
        if (!card.getManager().equals(currentUser)) {
            throw new SecurityException("카드의 작성자만 가능한 기능입니다.");
        }
    }

    private void maxCardCount(Long columnId) {
        TrelloColumn trelloColumn = trelloColumnService.findById(columnId);
        if (trelloColumn.getCards().size() >= 15) {
            throw new IllegalArgumentException("한 컬럼에 카드는 15가 최대입니다.");
        }
    }
    // 범위를 벗어났을때 에러처리
    private void validatePosition(Long trelloColumnId, int newPosition) {
        TrelloColumn trelloColumn = findTrelloColumn(trelloColumnId);
        if (newPosition < 0||newPosition >= trelloColumn.getCards().size()){
            throw new IllegalArgumentException("카드의 위치가 범위를 벗어났습니다."+trelloColumn.getCards().size()+"이내로 다시 입력해주세요");
        }
    }
}

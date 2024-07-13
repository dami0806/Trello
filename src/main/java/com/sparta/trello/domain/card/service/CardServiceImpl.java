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
    public CardResponse createCard(Long columnId, CardRequest cardRequest, String username) {
        User user = userService.getUserByName(username);
        TrelloColumn column = findTrelloColumn(columnId);

        maxCardCount(columnId);

        Card card = Card.builder()
                .manager(user)
                .title(cardRequest.getTitle())
                .description(cardRequest.getDescription())
                .trelloColumn(findTrelloColumn(columnId))
                .position(getPosition(columnId))
                .build();

        saveCard(card);

        // 컬럼의 카드 순서 업데이트
        List<Long> cardOrder = column.getCardOrder();
        cardOrder.add(card.getId());
        column.updateCardOrder(cardOrder);

        return cardMapper.toCardResponse(card);
    }

    //card 수정
    @Transactional
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
    public void updateCardPosition(Long cardId, int newPosition, Long newColumnId) {

        Card card = findCard(cardId);

        TrelloColumn currentColumn = card.getTrelloColumn();
        TrelloColumn newColumn = trelloColumnService.findById(newColumnId);

        maxCardCount(newColumn.getId());
        validatePosition(newColumnId, newPosition);

        // 기존 컬럼에서 카드 제거
        List<Long> currentCardOrder = currentColumn.getCardOrder();

        currentCardOrder.remove(card.getId());
        currentColumn.updateCardOrder(currentCardOrder);

        // 새 컬럼의 카드 순서 업데이트
        List<Long> newCardOrder = newColumn.getCardOrder();
        if (newPosition >= newCardOrder.size()) {
            newCardOrder.add(card.getId()); // 리스트 끝에 추가
        } else {
            newCardOrder.add(newPosition, card.getId()); // 지정된 위치에 추가
        }
        newColumn.updateCardOrder(newCardOrder);

        // 카드의 컬럼 변경
        card.updateColumn(newColumn);
        saveCard(card);
    }

    // card 삭제
    @Transactional
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

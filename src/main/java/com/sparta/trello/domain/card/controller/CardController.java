package com.sparta.trello.domain.card.controller;

import com.sparta.trello.domain.auth.service.AuthServiceImpl;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest, @AuthenticationPrincipal AuthServiceImpl userDetails) {
        CardResponse cardResponse = cardService.createCard(cardRequest, userDetails.getUser());
        return new ResponseEntity<>(cardResponse, HttpStatus.CREATED);
    }

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long cardId, @RequestBody CardRequest cardRequest, @AuthenticationPrincipal AuthServiceImpl userDetails) {
        CardResponse cardResponse = cardService.updateCard(cardId, cardRequest, userDetails.getUser());
        return new ResponseEntity<>(cardResponse, HttpStatus.OK);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId, @AuthenticationPrincipal AuthServiceImpl userDetails) {
        cardService.deleteCard(cardId, userDetails.getUser());
        return new ResponseEntity<>("카드 삭제하기 성공", HttpStatus.OK);
    }

    //카드 위치 변경 (인덱스 상, TrelloColumn에서 변경)
    @PutMapping("/{cardId}/position")
    public ResponseEntity<String> updateCardPosition(@PathVariable Long cardId, @RequestParam int newPosition, @RequestParam Long newColumnId, @AuthenticationPrincipal AuthServiceImpl userDetails) {
        cardService.updateCardPosition(cardId, newPosition, newColumnId, userDetails.getUser());
        return new ResponseEntity<>("카드 위치 옮기기 성공", HttpStatus.OK);
    }

    // 카드 상세 보기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long cardId, Pageable pageable) {
        CardResponse cardResponse = cardService.getCardById(cardId, pageable);
        return new ResponseEntity<>(cardResponse, HttpStatus.OK);
    }

}

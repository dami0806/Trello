package com.sparta.trello.domain.card.controller;

import com.sparta.trello.domain.auth.service.AuthServiceImpl;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
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

}

package com.sparta.trello.domain.card.controller;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.card.dto.CardDetailResponse;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.common.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/columns/{columnId}/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final BoardService boardService;


    // 카드 생성
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@PathVariable Long boardId,
                                                   @PathVariable Long columnId,
                                                   @RequestBody CardRequest cardRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        checkAuthAndRole(boardId,userDetails);

        CardResponse cardResponse = cardService.createCard(columnId,cardRequest, userDetails.getUsername());
        return new ResponseEntity<>(cardResponse, HttpStatus.CREATED);
    }

    // 카드 수정
    @PatchMapping("/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long boardId,
                                                   @PathVariable Long columnId,
                                                   @PathVariable Long cardId,
                                                   @RequestBody CardRequest cardRequest,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        checkAuthAndRole(boardId,userDetails);

        CardResponse cardResponse = cardService.updateCard(columnId,cardId, cardRequest);
        return new ResponseEntity<>(cardResponse, HttpStatus.OK);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long boardId,
                                             @PathVariable Long columnId,
                                             @PathVariable Long cardId,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        checkAuthAndRole(boardId,userDetails);
        cardService.deleteCard(cardId);
        return new ResponseEntity<>("카드 삭제하기 성공", HttpStatus.OK);
    }

    //카드 위치 변경 (인덱스 상, TrelloColumn에서 변경)
    @PutMapping("/{cardId}/position")
    public ResponseEntity<String> updateCardPosition(@PathVariable Long boardId,
                                                     @PathVariable Long columnId,
                                                     @PathVariable Long cardId,
                                                     @RequestParam int newPosition, @RequestParam Long newColumnId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        checkAuthAndRole(boardId,userDetails);
        cardService.updateCardPosition(cardId, newPosition, newColumnId);
        return new ResponseEntity<>("카드 위치 옮기기 성공", HttpStatus.OK);
    }

    // 카드 상세 보기
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDetailResponse> getCardById(@PathVariable Long boardId,
                                                    @PathVariable Long columnId,
                                                    @PathVariable Long cardId,
                                                    Pageable pageable,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        checkAuthAndRole(boardId,userDetails);
        CardDetailResponse cardResponse = cardService.getCardDetailById(cardId, pageable);
        return new ResponseEntity<>(cardResponse, HttpStatus.OK);
    }

    // 권한 검증
    private void checkBoardMemberOrManager(Long boardId, String username) {
        if (!boardService.isBoardMemberOrManager(boardId, username)) {
            throw new UnauthorizedException("해당 보드에 카드를 생성할 권한이 없습니다.");
        }
    }
    private void checkAuthAndRole(Long boardId,  UserDetails userDetails) {
        String username = userDetails.getUsername();
        SecurityUtils.validdateUserDetails(userDetails);
        checkBoardMemberOrManager(boardId, username);
    }
}

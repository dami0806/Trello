package com.sparta.trello.domain.column.controller;

import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.common.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.service.TrelloColumnServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards/{boardId}/columns")
@RequiredArgsConstructor
public class TrelloColumnController {
    private final TrelloColumnServiceImpl trelloColumnService;
	private final BoardService boardService;

	// 생성
    @PostMapping
    public ResponseEntity<?> createColumn(@PathVariable Long boardId,
                                          @RequestBody TrelloCreateColumnRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetails userDetails) {

		checkAuthAndRole(boardId,userDetails);

		TrelloCreateColumnRequestDto updatedRequestDto = new TrelloCreateColumnRequestDto(
				requestDto.columns_title(),
				boardId,
				requestDto.newPosition());
        return trelloColumnService.createColumn(updatedRequestDto);
    }

	// 삭제
    @DeleteMapping("/{columnId}")
    public ResponseEntity<?> deleteColumn(@PathVariable Long boardId, @PathVariable Long columnId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
		checkAuthAndRole(boardId,userDetails);


		return trelloColumnService.deleteColumn(boardId, columnId);
    }

	// 수정
    @PatchMapping("/{columnId}/restore")
    public ResponseEntity<?> restoreColumn(@PathVariable Long boardId, @PathVariable Long columnId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
		checkAuthAndRole(boardId,userDetails);


		return trelloColumnService.restoreColumn(boardId, columnId);
    }

	// 위치 이동
    @PatchMapping("/{columnId}/position")
    public ResponseEntity<?> moveColumn(@PathVariable Long boardId, @PathVariable Long columnId,
                                        @RequestParam int newPosition,
                                        @AuthenticationPrincipal UserDetails userDetails) {
		checkAuthAndRole(boardId,userDetails);

		return trelloColumnService.moveColumn(boardId, columnId, newPosition);
    }

	// 권한 검증
	private void checkBoardMemberOrManager(Long boardId, String username) {
		if (!boardService.isBoardMemberOrManager(boardId, username)) {
			throw new UnauthorizedException("해당 보드에 컬럼을 생성할 권한이 없습니다.");
		}
	}
	private void checkAuthAndRole(Long boardId,  UserDetails userDetails) {
		String username = userDetails.getUsername();
		SecurityUtils.validdateUserDetails(userDetails);
		checkBoardMemberOrManager(boardId, username);
	}
}
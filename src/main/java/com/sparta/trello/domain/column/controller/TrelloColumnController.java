package com.sparta.trello.domain.column.controller;

import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponse;

import com.sparta.trello.domain.common.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.sparta.trello.domain.auth.exception.UnauthorizedException;
import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.service.TrelloColumnServiceImpl;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
										  @RequestParam(required = false) Long targetPrevColumnId,
										  @AuthenticationPrincipal UserDetails userDetails) {
		checkAuthAndRole(boardId, userDetails);

		// DTO에서 boardId가 null일 경우 설정
		TrelloCreateColumnRequestDto updatedRequestDto = new TrelloCreateColumnRequestDto(
				requestDto.columns_title(),
				boardId,  // 여기에서 boardId 설정
				requestDto.newPosition()
		);

		return trelloColumnService.createColumn(updatedRequestDto, targetPrevColumnId);
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

	// 컬럼 상세보기
	@GetMapping("/{columnId}")
	public ResponseEntity<?> getColumnDetails(@PathVariable Long boardId,
											  @PathVariable Long columnId,
											  @AuthenticationPrincipal UserDetails userDetails) {
		checkAuthAndRole(boardId, userDetails);
		TrelloColumnResponse response = trelloColumnService.getColumnDetails(columnId);
		return ResponseEntity.ok(response);
	}


	// 위치 이동
	// 컬럼 위치 이동 (Linked List 기반)
	@PatchMapping("/{columnId}/position")
	public ResponseEntity<?> moveColumn(@PathVariable Long boardId,
										@PathVariable Long columnId,
										@RequestParam(required = false) Long targetPrevColumnId,
										@AuthenticationPrincipal UserDetails userDetails) {
		checkAuthAndRole(boardId, userDetails);
		return trelloColumnService.moveColumn(boardId, columnId, targetPrevColumnId);
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
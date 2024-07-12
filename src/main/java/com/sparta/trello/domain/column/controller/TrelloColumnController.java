package com.sparta.trello.domain.column.controller;

import org.springframework.http.HttpStatus;
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

import com.sparta.trello.domain.auth.service.UserDetailsServiceImpl;
import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.service.TrelloColumnServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards/{boardId}/columns")
@RequiredArgsConstructor
public class TrelloColumnController {
	private final TrelloColumnServiceImpl trelloColumnService;

	@PostMapping
	public ResponseEntity<?> createColumn(@PathVariable Long boardId,
		                                  @RequestBody TrelloCreateColumnRequestDto requestDto,
		                                  @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
		}

		TrelloCreateColumnRequestDto updatedRequestDto = new TrelloCreateColumnRequestDto(requestDto.columns_title(), boardId, requestDto.newPosition());
		return trelloColumnService.createColumn(updatedRequestDto);
	}

	@DeleteMapping("/{columnId}")
	public ResponseEntity<?> deleteColumn(@PathVariable Long boardId, @PathVariable Long columnId,
		                                  @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
		}

		return trelloColumnService.deleteColumn(boardId, columnId);
	}

	@PatchMapping("/{columnId}/restore")
	public ResponseEntity<?> restoreColumn(@PathVariable Long boardId, @PathVariable Long columnId,
									       @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
		}

		return trelloColumnService.restoreColumn(boardId, columnId);
	}

	@PatchMapping("/{columnId}/position")
	public ResponseEntity<?> moveColumn(@PathVariable Long boardId, @PathVariable Long columnId,
								      	@RequestParam int newPosition,
								    	@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 필요합니다.");
		}

		return trelloColumnService.moveColumn(boardId, columnId, newPosition);
	}
}
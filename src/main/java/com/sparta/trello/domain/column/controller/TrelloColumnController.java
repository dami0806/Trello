package com.sparta.trello.domain.column.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.service.TrelloColumnServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards/{boardId}/columns")
@RequiredArgsConstructor
public class TrelloColumnController {
	private final TrelloColumnServiceImpl trelloColumnService;

	@PostMapping
	public ResponseEntity<?> createColumn(@PathVariable Long boardId, @RequestBody TrelloCreateColumnRequestDto requestDto) {
		TrelloCreateColumnRequestDto updatedRequestDto = new TrelloCreateColumnRequestDto(requestDto.columns_title(), boardId);
		return trelloColumnService.createColumn(updatedRequestDto);
	}

	@DeleteMapping("/{columnId}")
	public ResponseEntity<?> deleteColumn(@PathVariable Long boardId, @PathVariable Long columnId) {
		return trelloColumnService.deleteColumn(boardId, columnId);
	}

	@PatchMapping("/{columnId}/restore")
	public ResponseEntity<?> restoreColumn(@PathVariable Long boardId, @PathVariable Long columnId) {
		return trelloColumnService.restoreColumn(boardId, columnId);
	}
}
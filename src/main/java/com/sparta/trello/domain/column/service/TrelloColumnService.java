package com.sparta.trello.domain.column.service;

import org.springframework.http.ResponseEntity;

import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;

public interface TrelloColumnService {
	ResponseEntity<?> createColumn(TrelloCreateColumnRequestDto requestDto);

	ResponseEntity<?> deleteColumn(Long boardId, Long columnId);

	ResponseEntity<?> restoreColumn(Long boardId, Long columnId);
}

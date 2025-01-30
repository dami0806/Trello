package com.sparta.trello.domain.column.service;

import com.sparta.trello.domain.column.dto.response.TrelloColumnResponse;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponseDto;
import org.springframework.http.ResponseEntity;

import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.entity.TrelloColumn;

public interface TrelloColumnService {
	ResponseEntity<?> createColumn(TrelloCreateColumnRequestDto requestDto, Long targetPrevColumnId);
	ResponseEntity<?> deleteColumn(Long boardId, Long columnId);

	ResponseEntity<?> restoreColumn(Long boardId, Long columnId);

	ResponseEntity<?> moveColumn(Long boardId, Long columnId, Long targetPrevColumnId);
	TrelloColumn findById(Long id);

	TrelloColumnResponse getColumnDetails(Long columnId);
}

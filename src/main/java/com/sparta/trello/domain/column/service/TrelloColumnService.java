package com.sparta.trello.domain.column.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponseDto;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.column.repository.TrelloColumnRepository;
import com.sparta.trello.exception.BusinessException;
import com.sparta.trello.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrelloColumnService {
	private final TrelloColumnRepository columnRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public ResponseEntity<?> createColumn(TrelloCreateColumnRequestDto requestDto) {
		columnRepository.findByTitle(requestDto.columns_title()).ifPresent(trelloColumn -> {
			throw new BusinessException(ErrorCode.COLUMN_ALREADY_EXISTS);
		});

		Optional<Board> optionalBoard = boardRepository.findById(requestDto.boardId());
		Board board = optionalBoard.orElseThrow(
			() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND)
		);

		TrelloColumn column = TrelloColumn.builder()
			.title(requestDto.columns_title())
			.board(board)
			.build();

		columnRepository.save(column);

		log.info("Status: {}, Message: {}", HttpStatus.CREATED.value(),"컬럼 생성 성공");

		TrelloColumnResponseDto responseDto = new TrelloColumnResponseDto(column.getTitle());

		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}
}
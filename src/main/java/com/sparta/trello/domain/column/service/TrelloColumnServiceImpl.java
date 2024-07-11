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

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrelloColumnServiceImpl implements TrelloColumnService {
	private final TrelloColumnRepository columnRepository;
	private final BoardRepository boardRepository;

	@Transactional
	@Override
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
			.deleted(false)
			.build();

		columnRepository.save(column);

		TrelloColumnResponseDto responseDto = new TrelloColumnResponseDto(column.getTitle());

		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@Transactional
	@Override
	public ResponseEntity<?> deleteColumn(Long boardId, Long columnId) {
		TrelloColumn column = columnRepository.findById(columnId).orElseThrow(
			() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND)
		);
		if (!Objects.equals(column.getBoard().getBoardId(), boardId)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
		}
		if (Boolean.TRUE.equals(column.getDeleted())) {
			throw new BusinessException(ErrorCode.COLUMN_ALREADY_DELETED);
		}
		column.softDelete();
		columnRepository.save(column);
		return ResponseEntity.ok("컬럼 삭제 성공");
	}

	@Transactional
	@Override
	public ResponseEntity<?> restoreColumn(Long boardId, Long columnId) {
		TrelloColumn column = columnRepository.findById(columnId).orElseThrow(
			() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND)
		);
		if (!Objects.equals(column.getBoard().getBoardId(), boardId)) {
			throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
		}
		if (Boolean.FALSE.equals(column.getDeleted())) {
			throw new BusinessException(ErrorCode.COLUMN_ALREADY_RESTORED);
		}
		column.restore();
		columnRepository.save(column);
		return ResponseEntity.ok("컬럼 복구 성공");
	}
}
package com.sparta.trello.domain.column.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.card.exception.DatabaseAccessException;
import com.sparta.trello.domain.column.dto.request.TrelloCreateColumnRequestDto;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponseDto;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.column.entity.TrelloColumnStatus;
import com.sparta.trello.domain.column.repository.TrelloColumnRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
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
			throw new DatabaseAccessException("COLUMN ALREADY EXISTS");
		});

		Optional<Board> optionalBoard = boardRepository.findById(requestDto.boardId());
		Board board = optionalBoard.orElseThrow(
			() -> new DatabaseAccessException("BOARD NOT FOUND")
		);

		int position = requestDto.newPosition();

		TrelloColumn column = TrelloColumn.builder()
			.title(requestDto.columns_title())
			.board(board)
			.status(TrelloColumnStatus.ACTIVE)
			.position(position)
			.build();

		List<TrelloColumn> columns = columnRepository.findByBoardAndStatusOrderByPosition(board, TrelloColumnStatus.ACTIVE);

		for (TrelloColumn col : columns) {
			if (col.getPosition() >= position) {
				col.updatePosition(col.getPosition() + 1);
			}
		}

		columnRepository.save(column);

		TrelloColumnResponseDto responseDto = new TrelloColumnResponseDto(column.getTitle());

		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@Transactional
	@Override
	public ResponseEntity<?> deleteColumn(Long boardId, Long columnId) {
		TrelloColumn column = checkBoardAndColumn(boardId, columnId, true);
		if (column.getStatus() == TrelloColumnStatus.DELETED) {
			throw new DatabaseAccessException("COLUMN ALREADY DELETED");
		}
		column.softDelete();
		columnRepository.save(column);
		return ResponseEntity.ok("컬럼 삭제 성공");
	}

	@Transactional
	@Override
	public ResponseEntity<?> restoreColumn(Long boardId, Long columnId) {
		TrelloColumn column = checkBoardAndColumn(boardId, columnId, true);
		if (column.getStatus() == TrelloColumnStatus.ACTIVE) {
			throw new DatabaseAccessException("COLUMN ALREADY RESTORED");
		}
		column.restore();
		columnRepository.save(column);
		return ResponseEntity.ok("컬럼 복구 성공");
	}

	@Transactional
	@Override
	public ResponseEntity<?> moveColumn(Long boardId, Long columnId, int newPosition) {
		checkBoardAndColumn(boardId, columnId, false);
		TrelloColumn column = columnRepository.findById(columnId).orElseThrow(
			() -> new DatabaseAccessException("COLUMN NOT FOUND")
		);
		List<TrelloColumn> columns = columnRepository.findByBoardAndStatusOrderByPosition(boardRepository.findById(boardId).get(), TrelloColumnStatus.ACTIVE);
		TrelloColumn targetColumn = null;

		for (TrelloColumn col : columns) {
			if (col.getPosition() == newPosition) {
				targetColumn = col;
				break;
			}
		}

		if (targetColumn != null) {
			int currentPos = column.getPosition();
			column.updatePosition(newPosition);
			targetColumn.updatePosition(currentPos);
			columnRepository.save(column);
			columnRepository.save(targetColumn);
		}
		columnRepository.saveAll(columns);
		return ResponseEntity.ok("컬럼 위치 이동");
	}

	//컬럼 id 찾기
	@Override
	public TrelloColumn findById(Long id) {
		return columnRepository.findById(id).orElse(null);
	}

	private int getNextPosition(Board board) {
		return columnRepository.countByBoardAndStatus(board, TrelloColumnStatus.ACTIVE);
	}

	private TrelloColumn checkBoardAndColumn(Long boardId, Long columnId, boolean checkColumn) {
		Board board = boardRepository.findById(boardId).orElseThrow(
			() -> new DatabaseAccessException("BOARD NOT FOUND")
		);
		TrelloColumn column = columnRepository.findById(columnId).orElseThrow(
			() -> new DatabaseAccessException("COLUMN NOT FOUND")
		);
		if (!Objects.equals(column.getBoard().getBoardId(), boardId)) {
			throw new DatabaseAccessException("UNAUTHORIZED USER");
		}
		if (checkColumn) {
			return column;
		}
		return null;
	}
}
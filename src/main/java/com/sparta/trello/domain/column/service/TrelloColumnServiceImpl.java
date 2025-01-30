package com.sparta.trello.domain.column.service;

import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.entity.CardStatus;
import com.sparta.trello.domain.card.mapper.CardMapper;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.card.service.CardServiceImpl;
import com.sparta.trello.domain.column.dto.response.TrelloColumnResponse;
import com.sparta.trello.domain.column.mapper.TrelloColumnMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrelloColumnServiceImpl implements TrelloColumnService {
    private final TrelloColumnRepository columnRepository;
    private final BoardRepository boardRepository;
	private final TrelloColumnMapper trelloColumnMapper;

    @Transactional
    @Override
    public ResponseEntity<?> createColumn(TrelloCreateColumnRequestDto requestDto, Long targetPrevColumnId) {
        columnRepository.findByTitle(requestDto.columns_title()).ifPresent(trelloColumn -> {
            throw new DatabaseAccessException("컬럼 이름이 중복됩니다.");
        });

        Board board = boardRepository.findById(requestDto.boardId())
                .orElseThrow(() -> new DatabaseAccessException("BOARD NOT FOUND"));

        TrelloColumn prevColumn = targetPrevColumnId != null ? columnRepository.findById(targetPrevColumnId).orElse(null) : null;
        TrelloColumn nextColumn = prevColumn != null ? prevColumn.getNextColumn() : null;

        // 새 컬럼 생성
        TrelloColumn column = TrelloColumn.builder()
                .title(requestDto.columns_title())
                .board(board)
                .status(TrelloColumnStatus.ACTIVE)
                .prevColumn(prevColumn)
                .nextColumn(nextColumn)
                .build();

        // Linked List 연결 업데이트
        if (prevColumn != null) {
            prevColumn.updateColumnOrder(prevColumn.getPrevColumn(), column);
            prevColumn.setNextColumn(column); // prevColumn의 next를 새 컬럼으로 설정
            columnRepository.save(prevColumn); // prevColumn 저장
        }

        if (nextColumn != null) {
            nextColumn.updateColumnOrder(column, nextColumn.getNextColumn());
            nextColumn.setPrevColumn(column); // nextColumn의 prev를 새 컬럼으로 설정
            columnRepository.save(nextColumn); // nextColumn 저장
        }

        columnRepository.save(column);
        return ResponseEntity.ok("컬럼 생성 성공");
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
    public ResponseEntity<?> moveColumn(Long boardId, Long columnId, Long targetPrevColumnId) {
        TrelloColumn column = columnRepository.findById(columnId)
                .orElseThrow(() -> new DatabaseAccessException("COLUMN NOT FOUND"));

        TrelloColumn targetPrevColumn = targetPrevColumnId != null
                ? columnRepository.findById(targetPrevColumnId).orElse(null)
                : null;

        TrelloColumn targetNextColumn = targetPrevColumn != null
                ? targetPrevColumn.getNextColumn()
                : null;

        // 기존 위치에서 컬럼 제거 (기존 prev, next 컬럼의 연결 복구)
        if (column.getPrevColumn() != null) {
            column.getPrevColumn().setNextColumn(column.getNextColumn());
            columnRepository.save(column.getPrevColumn());
        }
        if (column.getNextColumn() != null) {
            column.getNextColumn().setPrevColumn(column.getPrevColumn());
            columnRepository.save(column.getNextColumn());
        }

        // 새로운 위치에 삽입
        column.setPrevColumn(targetPrevColumn);
        column.setNextColumn(targetNextColumn);

        if (targetPrevColumn != null) {
            targetPrevColumn.setNextColumn(column);
            columnRepository.save(targetPrevColumn);
        }
        if (targetNextColumn != null) {
            targetNextColumn.setPrevColumn(column);
            columnRepository.save(targetNextColumn);
        }

        columnRepository.save(column);
        return ResponseEntity.ok("컬럼 위치 이동 성공");
    }



    //컬럼 id 찾기
    @Override
    public TrelloColumn findById(Long id) {
        return columnRepository.findById(id).orElse(null);
    }


    @Override
	@Transactional(readOnly = true)
    public TrelloColumnResponse getColumnDetails(Long columnId) {
        TrelloColumn column = checkColumn(columnId);

		List<Card> cards= column.getCards().stream()
				.filter(card -> card.getStatus() == CardStatus.ACTIVE)
				.collect(Collectors.toList());

		List<Long> cardOrder = column.getCardOrder();

		List<Card> orderedCards = cardOrder.stream()
				.map(orderId -> cards.stream()
						.filter(card -> card.getId().equals(orderId))
						.findFirst()
						.orElse(null))
						.filter(Objects::nonNull)
				.collect(Collectors.toList());

		column.updateOrderCars(orderedCards);

		return trelloColumnMapper.toTrelloColumnResponse(column);
    }

    private TrelloColumn checkColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(
                () -> new DatabaseAccessException("컬럼을 찾을수 없습니다.")
        );
    }

    private int getNextPosition(Board board) {
        return columnRepository.countByBoardAndStatus(board, TrelloColumnStatus.ACTIVE);
    }

    private TrelloColumn checkBoardAndColumn(Long boardId, Long columnId, boolean checkColumn) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new DatabaseAccessException("BOARD NOT FOUND")
        );

        TrelloColumn column = checkColumn(columnId);

        if (!Objects.equals(column.getBoard().getBoardId(), boardId)) {
            throw new DatabaseAccessException("UNAUTHORIZED USER");
        }
        if (checkColumn) {
            return column;
        }
        return null;
    }
}
package com.sparta.trello.domain.column.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.column.entity.TrelloColumn;
import com.sparta.trello.domain.column.entity.TrelloColumnStatus;

@Repository
public interface TrelloColumnRepository extends JpaRepository<TrelloColumn, Long> {
	Optional<TrelloColumn> findByTitle(String title);
	int countByBoardAndStatus(Board board, TrelloColumnStatus status);
	List<TrelloColumn> findByBoardAndStatusOrderByPosition(Board board, TrelloColumnStatus status);
}

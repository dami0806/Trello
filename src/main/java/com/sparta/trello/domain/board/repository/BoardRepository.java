package com.sparta.trello.domain.board.repository;

import java.util.List;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardStatus;
import com.sparta.trello.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findByUserAndBoardStatus(User user, BoardStatus boardStatus);
}

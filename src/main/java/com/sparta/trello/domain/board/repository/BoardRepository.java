package com.sparta.trello.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.trello.domain.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}

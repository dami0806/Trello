package com.sparta.trello.domain.column.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.trello.domain.column.entity.TrelloColumn;

@Repository
public interface TrelloColumnRepository extends JpaRepository<TrelloColumn, Long> {
	Optional<TrelloColumn> findByTitle(String title);
}

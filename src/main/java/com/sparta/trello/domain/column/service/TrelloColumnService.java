package com.sparta.trello.domain.column.service;

import com.sparta.trello.domain.column.entity.TrelloColumn;
import org.springframework.stereotype.Service;

import com.sparta.trello.domain.column.repository.TrelloColumnRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrelloColumnService {
	private TrelloColumnRepository columnRepository;

	//컬럼 id 찾기
	public TrelloColumn findById(Long id) {
		return columnRepository.findById(id).orElse(null);
	}


}
